package com.sg.shopping.service.center;

import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.UserInfoBO;

public interface CenterUserService {

    UserInfo queryUserInfo(String userId);
}
