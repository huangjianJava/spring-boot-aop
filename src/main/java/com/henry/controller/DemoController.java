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

    @ApiOperation(value = "post测试接口-简单dto", notes = "post测试接口-简单dto")
    @PostMapping(value = "/sign-demo/easy-dto", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestProcess(checkLogin = true, logUrl = "/sign-demo/easy-dto")
    public ResultData<String> modifyPhone(@Validated @RequestBody ModifyPasswordReqDto dto) {
        logger.error("controller modifyPhone=============================");
        return ResultData.createSuccessResult(null, "测试一下:" + dto.getPhone());
    }

    @ApiOperation(value = "hello测试接口", notes = "hello测试接口")
    @GetMapping(value = "/sign-demo/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestProcess(checkLogin = true, logUrl = "/demo/hello")
    public ResultData<String> modifyPhone2() {
        return ResultData.createSuccessResult(null, "hello_world");
    }

}
