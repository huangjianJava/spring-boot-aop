package com.henry;

import com.alibaba.fastjson.JSON;
import com.henry.common.utils.HttpUtil;
import com.henry.dto.ModifyPasswordReqDto;
import com.henry.utils.ObjectUtils;
import com.henry.utils.RsaCryptUtil;
import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author huangj
 * @version V1.0
 * @title: 一些简单的测试
 * @description:
 * @date 2018/8/28
 */
public class SimpleTest {

    private static final String SECRET_KEY = "321f3c83-231d-4739-8f8c-5860f1406377";

    public static final String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOdxKM4vLrMnzLXOyjcTCOA62gvNAKio3MA22V\n" +
            "FOhDXAuf8V1V81vVeHSrOv4UYB3aXuk4SbCdg/8XmJ8jK6nss4X/7KBdnFZrD/LswQedJeWcYlDe\n" +
            "gBcFV3Xp87AHBRjMHTFv0f4mpiqwZHmKb9iP2jIlLUBszMeylGO9WmOm5wIDAQAB";

    public static final String PRIVATEKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI53Eozi8usyfMtc7KNxMI4DraC8\n" +
            "0AqKjcwDbZUU6ENcC5/xXVXzW9V4dKs6/hRgHdpe6ThJsJ2D/xeYnyMrqeyzhf/soF2cVmsP8uzB\n" +
            "B50l5ZxiUN6AFwVXdenzsAcFGMwdMW/R/iamKrBkeYpv2I/aMiUtQGzMx7KUY71aY6bnAgMBAAEC\n" +
            "gYA9momf+GcYgw1plrYQ+toHHtoOnnbLiBxGstjI1XCV6BByyhkVGJWGWn8AqBLEVsex7Mame17u\n" +
            "zewZlPqDZ9cjCUMTdUHzsqSl14ZJXwsMK2ImvQW4LZ4nXPtQi5GEMJwRC3rSjV3RK5gZGGyBdm17\n" +
            "65C5MNhx+F34cLR6t73iSQJBAMUu6D8JVc84TeffobtUEF2RE6BXhoACR+sVaBsQp05y21p5QE9L\n" +
            "yUKD3/bPG+505k9oHLF8W+5velCIYlPelgMCQQC49dcqvHc0s5YtNtmMtktQHmLqNHvzVz6gysyr\n" +
            "7u8Z0xV8YHz64kqovMASs78H0S5evsCk3x0jQKmFVRGnSthNAkB5pWGUGf89MsAlL3rIZakfADR6\n" +
            "c+S/LikOJ4utPs3ogMVAgJMjGF8WUOMnkUfrCWVYUaN5/317kHyGGE2I7kXfAkAI+BLpBZ6aNaMw\n" +
            "qho+kpBwYCjXxpfEIxTsYyktpTZ/ABdoop0s5VBNgmml/oVi8afX/FQHKrKVBQuzZX6nqB95AkEA\n" +
            "peJ2R9PyU05ZqotyRSSLHEwa3ZJOhU0HhiQltUWNM614PO+n8SWaD4rJ1b/sPT2uxrW3GOVF7e/p\n" +
            "7zu3QEYL4g==\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOdxKM4vLrMnzLXOyjcTCOA62gvNAKio3MA22V\n" +
            "FOhDXAuf8V1V81vVeHSrOv4UYB3aXuk4SbCdg/8XmJ8jK6nss4X/7KBdnFZrD/LswQedJeWcYlDe\n" +
            "gBcFV3Xp87AHBRjMHTFv0f4mpiqwZHmKb9iP2jIlLUBszMeylGO9WmOm5wIDAQAB";

    @Test
    public void testSign() throws Exception {
        // 将参数进行字典排序
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = new TreeMap<>();
        map.put("orderId", "10086");
        map.put("mobile", "13632598743");
        // 组成待签名字符串
        sb.delete(0, sb.length()); // 清空StringBuilder
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.toString().contains("="))
                sb.append("&");
            sb.append(entry.getKey() + "=" + entry.getValue());
        }
        String signContent = sb.toString();
        System.out.println("待签名内容：" + signContent);

        // 1.用私钥生成签名
        String sign = RsaCryptUtil.sign(signContent.getBytes(), PRIVATEKEY);
        //System.out.println("私钥：" + PRIVATEKEY);
        System.out.println("签名：" + sign);
        System.out.println("签名长度：" + sign.length());
        String encoder = URLEncoder.encode(sign,"UTF-8");
        System.out.println("encoder：" + encoder);
        String decoder = URLDecoder.decode(encoder,"UTF-8");
        System.out.println("decoder：" + decoder);


        // 2.用公钥进行验签
        boolean verifySuccess = RsaCryptUtil.verify(signContent.getBytes(), PUBLICKEY, decoder);
        //System.out.println("公钥：" + PUBLICKEY);
        System.out.println("验签结果：" + verifySuccess);
    }

    public static void main(String[] args) throws Exception {
        ModifyPasswordReqDto dto = ModifyPasswordReqDto.builder()
                .phone("13632598743")
                .newLoginPassword("111")
                .oldLoginPassword("222")
                .build();
        String json = JSON.toJSONString(dto);
        long currentMillis = System.currentTimeMillis();
        Map<String, String> paramsMap = ObjectUtils.objectToMap(dto);
        paramsMap.put("timestamp",String.valueOf(currentMillis));
        paramsMap.put("appId","2014072300007148");
        paramsMap.put("secretKey",SECRET_KEY);
        String unencryptedSign = ObjectUtils.mapToString(paramsMap,"&");
        System.out.println("待签名的字符串:" + unencryptedSign);

        // 私钥生成签名
        String sign = RsaCryptUtil.sign(unencryptedSign.getBytes(), PRIVATEKEY);
        System.out.println("签名：" + sign);
        String encoder = URLEncoder.encode(sign,"UTF-8");
        System.out.println("url要传输的sign 签名：" + encoder);
        System.out.println("url中传输的sign 解码后签名：" + URLDecoder.decode(encoder,"UTF-8"));

        // 公钥进行验签
        /*boolean verifySuccess = RsaCryptUtil.verify(unencryptedSign.getBytes(), PUBLICKEY, URLDecoder.decode(encoder,"UTF-8"));
        System.out.println("验签结果：" + verifySuccess);*/

        String url = "http://localhost:8080/demo/sign-test?name=nike&age=18&timestamp=" + currentMillis + "&app_id=2014072300007148&sign=" + encoder;
        String response = HttpUtil.post(url,json);
        System.out.println("response:" + response);


    }

}
