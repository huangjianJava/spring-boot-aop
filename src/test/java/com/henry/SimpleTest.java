package com.henry;

import com.alibaba.fastjson.JSON;
import com.henry.common.utils.HttpUtil;
import com.henry.dto.ModifyPasswordReqDto;
import com.henry.dto.SignTestReqDto;
import com.henry.dto.SinterInterfaceReqDto;
import com.henry.utils.ObjectUtils;
import com.henry.utils.SinterUtil;
import org.junit.Test;

import java.util.Map;

/**
 * @author huangj
 * @version V1.0
 * @title: 一些简单的测试
 * @description:
 * @date 2018/8/28
 */
public class SimpleTest {

    public static void main(String[] args) throws Exception {
        // 组装 SinterInterfaceReqDto
        ModifyPasswordReqDto dto = ModifyPasswordReqDto.builder()
                .phone("13632598743")
                .newLoginPassword("111")
                .oldLoginPassword("222")
                .build();
        String requestData = JSON.toJSONString(dto);
        SinterInterfaceReqDto reqDto = SinterUtil.assembleSinterParamsTwo(requestData);
        System.out.println("simple 签名:" + reqDto.getDataSign());
        String reqJson = JSON.toJSONString(reqDto);

        // post
        String response = HttpUtil.post("http://localhost:8080/sign/test", reqJson);
        System.out.println("response:" + response);
    }

    @Test
    public void testObjectUtils() {
        ModifyPasswordReqDto dto = ModifyPasswordReqDto.builder()
                .phone("13632598743")
                .newLoginPassword("111")
                .oldLoginPassword("222")
                .build();
        SignTestReqDto signDto = SignTestReqDto.builder()
                .name("小米")
                .modifyPasswordReqDto(dto).build();
        String json = JSON.toJSONString(signDto);

        Map<String, Object> paramsMap = ObjectUtils.objectToMap2(signDto);
        String unencryptedSign = ObjectUtils.mapToString2(paramsMap, "&");
        System.out.println("待签名的字符串:" + unencryptedSign);
    }

}
