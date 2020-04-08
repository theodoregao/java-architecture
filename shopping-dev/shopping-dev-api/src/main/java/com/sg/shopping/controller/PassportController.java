package com.sg.shopping.controller;

import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.UserInfoBO;
import com.sg.shopping.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

    @GetMapping("/isNewUsernameAvailable")
    public JsonResult isNewUsernameAvailable(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            return JsonResult.errorMsg("username cannot be null.");
        }
        return userService.isUsernameExists(username) ?
                JsonResult.errorMsg("username already exist.") :
                JsonResult.ok();
    }

    @PostMapping("/register")
    public JsonResult register(@RequestBody UserInfoBO userInfoBO) {
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

        return JsonResult.ok(userService.createUser(userInfoBO));
    }

}
