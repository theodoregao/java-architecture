package com.sg.shopping.service.center;

import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.UserInfoBO;
import com.sg.shopping.pojo.bo.center.CenterUserBO;

public interface CenterUserService {

    UserInfo queryUserInfo(String userId);

    UserInfo updateUserInfo(String userId, CenterUserBO centerUserBO);
}
