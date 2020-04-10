package com.sg.shopping.service;

import com.sg.shopping.pojo.UserAddress;

import java.util.List;

public interface AddressService {

    List<UserAddress> queryAll(String userId);
}
