package com.sg.shopping.service.center;

import com.sg.shopping.common.utils.PagedGridResult;
import com.sg.shopping.pojo.OrderStatus;

public interface MyOrdersService {

    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    OrderStatus updateDeliverOrderStatus(String orderId);
}
