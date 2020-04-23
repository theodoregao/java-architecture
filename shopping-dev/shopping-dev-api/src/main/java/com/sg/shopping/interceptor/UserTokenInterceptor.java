package com.sg.shopping.interceptor;

import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.common.utils.JsonUtils;
import com.sg.shopping.common.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class UserTokenInterceptor implements HandlerInterceptor {

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {
            String uniqueToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
            if (StringUtils.isBlank(uniqueToken)) {
                System.out.println("Operation intercepted and user not login. Please login first");
                returnErrorResponse(response, JsonResult.errorException("Please login"));
                return false;
            } else {
                if (!uniqueToken.equals(userToken)) {
                    System.out.println("User was logged in a different place.");
                    returnErrorResponse(response, JsonResult.errorException("User was logged in a different place."));
                    return false;
                }
            }
        } else {
            System.out.println("Operation intercepted and user not login. Please login first");
            returnErrorResponse(response, JsonResult.errorException("Please login"));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public void returnErrorResponse(HttpServletResponse response, JsonResult result) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
