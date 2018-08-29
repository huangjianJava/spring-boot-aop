package com.henry.common.aop;

import com.alibaba.fastjson.JSON;
import com.henry.common.constants.ShareConstants;
import com.henry.common.utils.ServletUtil;
import com.henry.result.ResultData;
import com.henry.result.enums.TokenResultEnum;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangj
 * @version V1.0
 * @title: 请求拦截 AOP
 * @description:
 * @date 2018/8/28
 */
public abstract class RequestProcessAop {

    private static Logger logger = LoggerFactory.getLogger(RequestProcessAop.class);

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    private static final String PRE_TAG = "";

    protected abstract boolean getCheckToken();

    protected abstract boolean checkLoginAuth(String tokenStr);

    protected abstract boolean checkPopedom(RequestProcess pjp);

    /**
     * 日志数据处理
     *
     * @param logData
     */
    protected boolean processLog(Map<String, Object> logData) {
        return false;
    }

    /**
     * 校验数据权限
     *
     * @param popedomCode 权限
     * @return boolean
     */
    protected boolean checkDataPopedom(String popedomCode) {
        return true;
    }

    /**
     * 是否日志记录
     */
    protected boolean checkEnableLog() {
        return true;
    }

    protected TokenResultEnum checkLoginAuthEx(String tokenStr, RequestProcess pjp) {
        return TokenResultEnum.TOKEN_OK;
    }

    protected TokenResultEnum checkOutSign(RequestProcess pjp,ProceedingJoinPoint point) {
        return TokenResultEnum.TOKEN_OK;
    }

    // 定义切点(RequestProcess注解)
    @Pointcut("@annotation(com.henry.common.aop.RequestProcess)")
    public void webLog() {
    }

    // 前置通知
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        logger.debug(PRE_TAG + "doBefore");
    }

    // 正常返回后通知
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        logger.debug(PRE_TAG + "doAfterReturning");
    }

    // 环绕通知 ProceedingJoinPoint表示连接点对象
    @Around("webLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug(PRE_TAG + "around");
        startTime.set(System.currentTimeMillis());
        Object ret;
        Map<String, Object> logObj = null;

        try {
            // 请求日志记录
            if (checkEnableLog()) {
                logger.debug("---");
                logObj = new HashMap<>(16);

                ServletRequestAttributes attributes =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();

                logObj.put("HEADER", ServletUtil.getHeadersInfo(request));
                logObj.put("URL", request.getRequestURL().toString());
                logObj.put("HTTP_METHOD", request.getMethod());
                logObj.put("IP", ServletUtil.getRemoteIp());
                logObj.put("CLASS_METHOD",
                        pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());

                Object[] args = pjp.getArgs();
                if (args != null) {
                    try {
                        ArrayList<Object> newArgs = new ArrayList<>();
                        for (Object arg : args) {
                            if ((arg == null) || (arg instanceof HttpServletRequest)
                                    || (arg instanceof HttpServletResponse)
                                    || (arg instanceof MultipartFile)) {
                                continue;
                            }

                            newArgs.add(arg);
                        }
                        logObj.put("ARGS", newArgs);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        logger.error(ex.getMessage());
                    }
                }
            }

            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method targetMethod = methodSignature.getMethod();
            RequestProcess obj = targetMethod.getAnnotation(RequestProcess.class);
            ApiOperation apiOperObj = targetMethod.getAnnotation(ApiOperation.class);
            RequestMapping requestMapObj = targetMethod.getAnnotation(RequestMapping.class);

            if (checkEnableLog() && (null != logObj)) {
                if (apiOperObj != null) {
                    logObj.put("LOGNAME", apiOperObj.value());
                }
                if (requestMapObj != null) {
                    logObj.put("LOGURL", StringUtils.join(requestMapObj.value(), "/"));
                }
            }

            // @RequestProcess 注解权限控制
            if (obj != null) {
                if (getCheckToken() && obj.checkLogin()) {
                    ServletRequestAttributes attributes =
                            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    HttpServletRequest request = attributes.getRequest();
                    String tokenStr = getTokenStr(request);

                    //用于兼容1.0.30-SNAPSHOT前的版本
                    if (!checkLoginAuth(tokenStr)) {
                        return ResultData.createAuthFailResult();
                    }

                    //1.0.30-SNAPSHOT加入
                    TokenResultEnum tokenResult = checkLoginAuthEx(tokenStr, obj);
                    if (null == tokenResult) {
                        return ResultData.createAuthFailResult();
                    } else {
                        switch (tokenResult) {
                            case TOKEN_OK:
                                break;
                            case TOKEN_FAIL:
                                return ResultData.createAuthFailResult();
                            case TOKEN_LOSE:
                                return ResultData.createTokenLoseResult();
                            case TOKEN_KICK:
                                return ResultData.createTokenKickResult();
                            default:
                                return ResultData.createAuthFailResult();
                        }
                    }

                    // 校验功能权限
                    if (!checkPopedom(obj)) {
                        String message =
                                apiOperObj == null
                                        ? pjp.getSignature().getDeclaringTypeName() + "."
                                        + pjp.getSignature().getName()
                                        : apiOperObj.value();
                        return ResultData.createPopedomFailResult(message);
                    }

                    // 校验数据权限
                    if (obj.checkDataPopedom()) {
                        if (!checkDataPopedom(obj.popedomCode())) {
                            String message =
                                    apiOperObj == null
                                            ? pjp.getSignature().getDeclaringTypeName() + "."
                                            + pjp.getSignature().getName()
                                            : apiOperObj.value();
                            return ResultData.createDataPopedomFailResult(message);
                        }
                    }
                }

                // 自测试 -》用于对外接口签名校验
                if(obj.checkSign()){
                    TokenResultEnum tokenResultTest = checkOutSign(obj, pjp);
                    if (null == tokenResultTest) {
                        return ResultData.createAuthFailResult();
                    } else {
                        switch (tokenResultTest) {
                            case TOKEN_OK:
                                break;
                            case TOKEN_FAIL:
                                return ResultData.createAuthFailResult();
                            case TOKEN_LOSE:
                                return ResultData.createTokenLoseResult();
                            default:
                                return ResultData.createAuthFailResult();
                        }
                    }
                }
            } else {
                return ResultData.createAuthFailResult();
            }
            ret = pjp.proceed();
        } finally {
            long execTimes = System.currentTimeMillis() - startTime.get();
            if (checkEnableLog() && (null != logObj)) {
                logObj.put("EXECTIMES", execTimes);
                if (processLog(logObj) == false) {
                    logger.info(JSON.toJSONString(logObj));
                }
            }
        }
        return ret;
    }

    /**
     * 获取token
     *
     * @param request
     * @return
     */
    private String getTokenStr(HttpServletRequest request) {
        String tokenStr = request.getHeader(ShareConstants.TOKEN_HEADER_NAME);
        if (tokenStr == null) {
            tokenStr = request.getParameter(ShareConstants.TOKEN_GET_NAME);
        }
        return tokenStr;
    }
}
