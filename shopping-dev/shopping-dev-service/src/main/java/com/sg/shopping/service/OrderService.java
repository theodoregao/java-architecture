package com.sg.shopping.service;

import com.sg.shopping.pojo.OrderStatus;
import com.sg.shopping.pojo.bo.SubmitOrderBO;
import com.sg.shopping.pojo.vo.OrderVO;

public interface OrderService {

    OrderVO createOrder(SubmitOrderBO submitOrderBO);

    void updateOrderStatus(String orderId, Integer orderStatus);

    OrderStatus queryOrderStatusInfo(String orderId);

    void closeOrder();
}
