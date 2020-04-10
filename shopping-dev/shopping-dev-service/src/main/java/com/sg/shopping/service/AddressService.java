package com.sg.shopping.service;

import com.sg.shopping.pojo.UserAddress;
import com.sg.shopping.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    List<UserAddress> queryAll(String userId);

    void addNewUserAddress(AddressBO addressBO);

    void updateUserAddress(AddressBO addressBO);

    void deleteUserAddress(String userId, String addressId);

    void updateUserAddressToBeDefaultAddress(String userId, String addressId);
}
