package com.henry.advice;

import com.google.common.base.Charsets;
import com.henry.common.aop.RequestProcess;
import com.henry.common.aop.RequestProcessAop;
import com.henry.dto.SinterInterfaceReqDto;
import com.henry.result.enums.TokenResultEnum;
import com.henry.utils.SinterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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

    /**
     * key:用户ID value:加密私钥  模拟数据库
     */
    public final static Map<String, String> BUSINESS_INFO = new HashMap<String, String>() {{
        put("1342581", "27af266a-124e-417c-9936-2946b355ff1f");
        put("1341111", "28af266a-224e-517c-9136-3946b355ff1d");
    }};

    @Override
    protected boolean getCheckToken() {
        return true;
    }

    @Override
    protected boolean checkLoginAuth(String tokenStr) {
        return true;
    }

    @Override
    protected TokenResultEnum checkOutSign(RequestProcess pjp, ProceedingJoinPoint point) {
        System.out.println("============== 签名校验");
        try {
            boolean checkSuccess = checkSign(point);
            if (checkSuccess) {
                return TokenResultEnum.TOKEN_OK;
            } else {
                return TokenResultEnum.TOKEN_FAIL;
            }
        } catch (Exception e) {
            logger.error("签名校验失败:" + e.getMessage());
            return TokenResultEnum.TOKEN_FAIL;
        }
    }

    @Override
    protected boolean checkPopedom(RequestProcess process) {
        return true;
    }

    /**
     * 签名校验
     * 算法参考 "快递鸟"
     * @param point
     * @return
     * @throws Exception
     */
    private boolean checkSign(ProceedingJoinPoint point) throws Exception {
        Object[] objects = point.getArgs();
        int length = objects.length;
        if (length == 0) {
            log.error("签名校验失败 -> 传输数据为空");
            return false;
        }
        SinterInterfaceReqDto reqDto = (SinterInterfaceReqDto) objects[0];
        log.info("签名校验入参 -> reqDto:" + reqDto);
        if (StringUtils.isEmpty(reqDto.getBusinessID()) || StringUtils.isEmpty(reqDto.getDataType())) {
            log.error("签名校验失败 -> 入参有误");
            return false;
        }
        String apiKey = BUSINESS_INFO.get(reqDto.getBusinessID());
        if (StringUtils.isEmpty(apiKey)) {
            log.error("签名校验失败 -> 非法用户,用户ID:" + reqDto.getBusinessID());
            return false;
        }

        // 组装待签名的字符串(post中有参数的时候参与签名)
        String sourceDataSign = reqDto.getDataSign();
        String requestData = reqDto.getRequestData();
        String dataSign = SinterUtil.encrypt(requestData, apiKey, Charsets.UTF_8.name());
        if (!sourceDataSign.equals(dataSign)) {
            System.out.println("checkSignTwo 签名校验失败");
            log.error("签名校验失败 -> 传过来的签名:" + sourceDataSign + ",校验的签名:" + dataSign);
            return false;
        } else {
            logger.info("签名校验成功");
            return true;
        }
    }

}
