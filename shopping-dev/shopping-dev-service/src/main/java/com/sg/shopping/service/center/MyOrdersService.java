package com.sg.shopping.service.center;

import com.sg.shopping.common.utils.PagedGridResult;
import com.sg.shopping.pojo.OrderStatus;
import com.sg.shopping.pojo.Orders;

public interface MyOrdersService {

    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    OrderStatus updateDeliverOrderStatus(String orderId);

    Orders queryOrders(String userId, String orderId);

    OrderStatus updateConfirmReceiveOrderStatus(String orderId);

    boolean updateDeleteOrderStatus(String userId, String orderId);
}
