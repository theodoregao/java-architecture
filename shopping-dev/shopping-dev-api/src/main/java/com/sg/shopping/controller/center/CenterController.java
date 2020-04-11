package com.sg.shopping.controller.center;

import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.controller.BaseController;
import com.sg.shopping.service.center.CenterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "User center related APIs", tags = {"User center related APIs tag"})
@RestController
@RequestMapping("center")
public class CenterController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @GetMapping("userInfo")
    @ApiOperation(value = "user info", notes = "get user info", httpMethod = "GET")
    public JsonResult userInfo(@ApiParam(name = "userId", value = "user id", required = true) String userId) {
        return JsonResult.ok(centerUserService.queryUserInfo(userId));
    }
}
