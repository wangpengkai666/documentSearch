package com.example.documentseach.rest.controller.v1;

import com.example.documentseach.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangpengkai
 */
@RestController("healthController")
@RequestMapping("/health")
@Api(tags = "服务健康度探活")
public class HealthController {
    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "api探活接口")
    public Result<String> demo() {
        return Result.buildSucc("let's go search");
    }
}
