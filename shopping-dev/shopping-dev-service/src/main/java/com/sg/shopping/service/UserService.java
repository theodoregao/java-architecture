package com.sg.shopping.service;

import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.UserInfoBO;

public interface UserService {

    boolean isUsernameExists(String username);

    UserInfo createUser(UserInfoBO userInfoBO);

    UserInfo login(String username, String password);
}
