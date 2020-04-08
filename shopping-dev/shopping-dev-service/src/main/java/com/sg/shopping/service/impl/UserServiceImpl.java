package com.sg.shopping.service.impl;

import com.sg.shopping.common.enums.Sex;
import com.sg.shopping.common.utils.DateUtil;
import com.sg.shopping.common.utils.MD5Utils;
import com.sg.shopping.mapper.UserInfoMapper;
import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.UserInfoBO;
import com.sg.shopping.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_USER_FACE = "https://www.svgimages.com/svg-image/s2/user-man-icon-256x256.png";
    private static final Date DEFAULT_BIRTHDAY = DateUtil.stringToDate("1900-01-01");

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean isUsernameExists(String username) {
        Example userExample = new Example(UserInfo.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);
        UserInfo userInfo = userInfoMapper.selectOneByExample(userExample);
        return userInfo != null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserInfo createUser(UserInfoBO userInfoBO) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(sid.nextShort());
        userInfo.setUsername(userInfoBO.getUsername());
        try {
            userInfo.setPassword(MD5Utils.getMd5String(userInfoBO.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        userInfo.setNickname(userInfoBO.getUsername());
        userInfo.setFace(DEFAULT_USER_FACE);
        userInfo.setBirthday(DEFAULT_BIRTHDAY);
        userInfo.setSex(Sex.SECRET.type);
        userInfo.setCreatedTime(new Date());
        userInfo.setUpdatedTime(new Date());
        userInfoMapper.insert(userInfo);
        return userInfo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserInfo login(String username, String password) {
        Example userExample = new Example(UserInfo.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);
        userCriteria.andEqualTo("password", password);
        return userInfoMapper.selectOneByExample(userExample);
    }
}
