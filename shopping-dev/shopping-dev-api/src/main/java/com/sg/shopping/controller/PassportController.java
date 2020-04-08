package com.sg.shopping.controller;

import com.sg.shopping.common.utils.CookieUtils;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.common.utils.JsonUtils;
import com.sg.shopping.common.utils.MD5Utils;
import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.UserInfoBO;
import com.sg.shopping.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("passport")
@Api(value = "Register & login", tags = {"Provide APIs for register and login"})
public class PassportController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Check whether the new username is available", httpMethod = "GET")
    @GetMapping("/isNewUsernameAvailable")
    public JsonResult isNewUsernameAvailable(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            return JsonResult.errorMsg("username cannot be null.");
        }
        return userService.isUsernameExists(username) ?
                JsonResult.errorMsg("username already exist.") :
                JsonResult.ok();
    }

    @ApiOperation(value = "Register", notes = "Register a new user with username and password", httpMethod = "POST")
    @PostMapping("/register")
    public JsonResult register(@RequestBody UserInfoBO userInfoBO,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        String username = userInfoBO.getUsername();
        String password = userInfoBO.getPassword();
        String confirmPassword = userInfoBO.getConfirmPassword();

        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)) {
            return JsonResult.errorMsg("username or password cannot be null");
        }

        if (userService.isUsernameExists(username)) {
            return JsonResult.errorMsg("username already exist.");
        }

        if (password.length() < 6) {
            return JsonResult.errorMsg("password length cannot less than 6");
        }

        if (!password.equals(confirmPassword)) {
            return JsonResult.errorMsg("password need to be the same as password confirm");
        }

        UserInfo userInfo = userService.createUser(userInfoBO);

        if (userInfo != null) {
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userInfo), true);
        }

        return JsonResult.ok(userInfo);
    }

    @ApiOperation(value = "Check whether the new username is available", httpMethod = "POST")
    @PostMapping("/login")
    public JsonResult login(@RequestBody UserInfoBO userInfoBO,
                            HttpServletRequest request,
                            HttpServletResponse response) throws NoSuchAlgorithmException {
        String username = userInfoBO.getUsername();
        String password = userInfoBO.getPassword();

        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return JsonResult.errorMsg("username or password cannot be null");
        }

        UserInfo userInfo = userService.login(username, MD5Utils.getMd5String(password));

        if (userInfo == null) {
            return JsonResult.errorMsg("Username or password incorrect");
        }

        userInfo = setNullProperty(userInfo);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userInfo), true);

        return JsonResult.ok(userInfo);
    }

    @ApiOperation(value = "User logout", httpMethod = "POST")
    @PostMapping("/logout")
    public JsonResult logout(@RequestParam String userId,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, "user");
        return JsonResult.ok();
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

}
