package com.henry.controller;

import com.henry.common.aop.RequestProcess;
import com.henry.dto.SinterInterfaceReqDto;
import com.henry.result.ResultData;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangj
 * @version V1.0
 * @Title:
 * @Description: 测试接口
 * @date 2018/8/28
 */
@RestController
public class SignInterfaceController {

    private static Logger logger = LoggerFactory.getLogger(SignInterfaceController.class);

    @ApiOperation(value = "签名测试", notes = "签名测试")
    @PostMapping(value = "/sign/test", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestProcess(checkLogin = false, checkSign = true, logUrl = "/sign/test")
    public ResultData<String> signTest(@Validated @RequestBody SinterInterfaceReqDto dto) {
        logger.error("SignInterfaceController signTest=============================");
        return ResultData.createSuccessResult(null, "用户ID:" + dto.getBusinessID());
    }

}
