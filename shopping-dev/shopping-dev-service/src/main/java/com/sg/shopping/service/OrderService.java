package com.sg.shopping.service;

import com.sg.shopping.pojo.bo.SubmitOrderBO;

public interface OrderService {

    String createOrder(SubmitOrderBO submitOrderBO);

    void updateOrderStatus(String orderId, Integer orderStatus);
}
