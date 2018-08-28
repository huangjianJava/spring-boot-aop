package com.henry.advice;

import com.google.common.collect.Maps;
import com.henry.common.aop.RequestProcess;
import com.henry.common.aop.RequestProcessAop;
import com.henry.result.enums.TokenResultEnum;
import com.henry.utils.ObjectUtils;
import com.henry.utils.RsaCryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author huangj
 * @version V1.0
 * @title: 请求拦截 切面
 * @description:
 * @date 2018/8/28
 */
@Component
@Order(2)
@Aspect
@Slf4j
public class WebLogAopAspect extends RequestProcessAop {

    private static Logger logger = LoggerFactory.getLogger(WebLogAopAspect.class);

    private static final String SECRET_KEY = "321f3c83-231d-4739-8f8c-5860f1406377";

    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOdxKM4vLrMnzLXOyjcTCOA62gvNAKio3MA22V\n" +
            "FOhDXAuf8V1V81vVeHSrOv4UYB3aXuk4SbCdg/8XmJ8jK6nss4X/7KBdnFZrD/LswQedJeWcYlDe\n" +
            "gBcFV3Xp87AHBRjMHTFv0f4mpiqwZHmKb9iP2jIlLUBszMeylGO9WmOm5wIDAQAB";

    @Override
    protected boolean getCheckToken() {
        return true;
    }

    @Override
    protected boolean checkLoginAuth(String tokenStr) {
        return true;
    }

    @Override
    protected TokenResultEnum checkLoginAuthEx(String tokenStr, RequestProcess pjp, ProceedingJoinPoint point) {
        logger.info("执行了重载的 checkLoginAuthEx");

        boolean checkSuccess = checkSign(point);
        if (checkSuccess) {
            logger.info("签名校验成功");
            return TokenResultEnum.TOKEN_OK;
        } else {
            logger.info("签名校验失败");
            return TokenResultEnum.TOKEN_FAIL;
        }
    }

    @Override
    protected boolean checkPopedom(RequestProcess process) {
        return true;
    }

    /**
     * 签名校验
     *
     * @param point
     * @return
     */
    private boolean checkSign(ProceedingJoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String timestamp = request.getParameter("timestamp");
        String appId = request.getParameter("app_id");
        String sign = request.getParameter("sign");
        log.info("checkSign入参 -> timestamp:" + timestamp + ",app_id:" + appId + ",sign:" + sign);
        if (StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(sign)) {
            log.error("checkSign入参有误");
            return false;
        }

        // 请求有效时间
        long currentMillis = System.currentTimeMillis();
        Long diff = currentMillis - Long.valueOf(timestamp);
        if (Math.abs(diff) > 120000) {
            log.error("签名校验失败:请求时间戳有误,diff millis:" + diff);
            return false;
        }

        // 组装待签名的字符串(post中有参数的时候参与签名)
        String unencryptedSign;
        Map<String, String> paramsMap = Maps.newHashMap();
        Object[] objects = point.getArgs();
        int length = objects.length;
        if (length > 0) {
            Object obj = objects[0];
            paramsMap = ObjectUtils.objectToMap(obj);
            if (paramsMap == null) {
                return false;
            }
        }
        paramsMap.put("timestamp", timestamp);
        paramsMap.put("appId", appId);
        paramsMap.put("secretKey", SECRET_KEY);
        unencryptedSign = ObjectUtils.mapToString(paramsMap, "&");
        logger.info("aspect 待签名的字符串:" + unencryptedSign);

        // 公钥验签
        boolean verifySuccess = false;
        try {
            verifySuccess = RsaCryptUtil.verify(unencryptedSign.getBytes(), PUBLIC_KEY, sign);
        } catch (Exception e) {
            logger.error("签名校验失败:" + e.getMessage());
        }
        return verifySuccess;
    }

}
