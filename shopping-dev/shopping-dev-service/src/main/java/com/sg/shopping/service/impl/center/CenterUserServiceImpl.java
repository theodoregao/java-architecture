package com.sg.shopping.service.impl.center;

import com.sg.shopping.mapper.UserInfoMapper;
import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.center.CenterUserBO;
import com.sg.shopping.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public UserInfo queryUserInfo(String userId) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        userInfo.setPassword(null);
        return userInfo;
    }

    @Override
    public UserInfo updateUserInfo(String userId, CenterUserBO centerUserBO) {
        UserInfo updatedUser = new UserInfo();
        BeanUtils.copyProperties(centerUserBO, updatedUser);
        updatedUser.setId(userId);
        updatedUser.setUpdatedTime(new Date());
        userInfoMapper.updateByPrimaryKeySelective(updatedUser);
        return queryUserInfo(userId);
    }
}
