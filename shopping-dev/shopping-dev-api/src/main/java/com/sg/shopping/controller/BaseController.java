package com.sg.shopping.controller;

import com.sg.shopping.common.utils.RedisOperator;
import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class BaseController {
    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String FOODIE_SHOPCART = "shopcart";

    public static String PAY_RETURN_URL = "http://sht4ej.natappfree.cc/shopping-dev-api/orders/notifyPaid";

    public static final String PAYMENT_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    public static final String IMOOC_USERID = "1237820-357889551";
    public static final String IMOOC_PASSCODE = "pwo1-e0o3-0o2d-10ld";

//    public static final String IMAGE_USER_FACE_LOCATION = "E:\\temp\\face";

    @Autowired
    private RedisOperator redisOperator;

    protected void updateReturnUrl(String url) {
        PAY_RETURN_URL = "http://" + url + ".natappfree.cc/shopping-dev-api/orders/notifyPaid";
    }

    protected UserInfoVO convertToUserInfoVo(UserInfo userInfo) {
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + userInfo.getId(), uniqueToken);

        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);
        userInfoVO.setUserUniqueToken(uniqueToken);
        return userInfoVO;
    }
}
