package com.sg.shopping.controller;

import com.sg.shopping.common.utils.*;
import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.UserInfoBO;
import com.sg.shopping.pojo.vo.UserInfoVO;
import com.sg.shopping.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Controller
public class SsoController {
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/login")
    public String login(String returnUrl, Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        model.addAttribute("returnUrl", returnUrl);
        return "login"; // this will redirect to login.html resource file.
    }


    @PostMapping("/doLogin")
    public String doLogin(String username,
                            String password,
                            String returnUrl,
                            Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) throws NoSuchAlgorithmException {
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            model.addAttribute("errmsg", "password cannot be null");
            return "login";
        }

        UserInfo userInfo = userService.login(username, MD5Utils.getMd5String(password));

        if (userInfo == null) {
            model.addAttribute("errmsg", "user or password incorrect");
            return "login";
        }

        String uniqueToken = UUID.randomUUID().toString().trim();
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);
        userInfoVO.setUserUniqueToken(uniqueToken);
        redisOperator.set(REDIS_USER_TOKEN + ":" + userInfo.getId(), JsonUtils.objectToJson(userInfoVO));

        return "login";
    }

}
