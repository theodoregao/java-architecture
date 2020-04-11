package com.sg.shopping.controller.center;

import com.sg.shopping.common.utils.CookieUtils;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.common.utils.JsonUtils;
import com.sg.shopping.controller.BaseController;
import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.center.CenterUserBO;
import com.sg.shopping.service.center.CenterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "Center User info related APIs", tags = {"Center User info related APIs tag"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @PostMapping("update")
    @ApiOperation(value = "user info", notes = "get user info", httpMethod = "GET")
    public JsonResult userInfo(
            @ApiParam(name = "userId", value = "user id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO userBO,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return JsonResult.errorMap(getErrors(bindingResult));
        }
        UserInfo userInfo = centerUserService.updateUserInfo(userId, userBO);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userInfo), true);

        return JsonResult.ok(userInfo);
    }

    private UserInfo setNullProperty(UserInfo userInfo) {
        userInfo.setPassword(null);
        userInfo.setMobile(null);
        userInfo.setEmail(null);
        userInfo.setBirthday(null);
        userInfo.setCreatedTime(null);
        userInfo.setUpdatedTime(null);
        return userInfo;
    }

    private Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error: errors) {
            String errorField = error.getField();
            String errorMsg = error.getDefaultMessage();
            map.put(errorField, errorMsg);
        }
        return map;
    }
}
