package com.henry.controller;

import com.henry.common.aop.RequestProcess;
import com.henry.dto.ModifyPasswordReqDto;
import com.henry.result.ResultData;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangj
 * @version V1.0
 * @Title:
 * @Description: 测试接口
 * @date 2018/8/28
 */
@RestController
public class DemoController {

    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @ApiOperation(value = "测试接口", notes = "测试接口")
    @PostMapping(value = "/demo/sign-test", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestProcess(checkLogin = true, logUrl = "/demo/sign-test")
    public ResultData<String> modifyPhone(@Validated @RequestBody ModifyPasswordReqDto dto) {
        logger.error("controller modifyPhone=============================");
        return ResultData.createSuccessResult(null, "测试一下:" + dto.getPhone());
    }

    @ApiOperation(value = "测试接口2", notes = "测试接口2")
    @GetMapping(value = "/demo/sign-test2", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestProcess(checkLogin = true, logUrl = "/demo/sign-test")
    public ResultData<String> modifyPhone2() {
        logger.error("controller modifyPhone2=============================");
        return ResultData.createSuccessResult(null, "hello_world");
    }

}
