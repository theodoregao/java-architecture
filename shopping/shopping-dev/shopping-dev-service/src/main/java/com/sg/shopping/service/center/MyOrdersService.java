package com.sg.shopping.service.center;

import com.sg.shopping.common.utils.PagedGridResult;
import com.sg.shopping.pojo.OrderStatus;
import com.sg.shopping.pojo.Orders;
import com.sg.shopping.pojo.vo.OrderStatusCountsVO;

import java.util.List;

public interface MyOrdersService {

    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    OrderStatus updateDeliverOrderStatus(String orderId);

    Orders queryOrders(String userId, String orderId);

    OrderStatus updateConfirmReceiveOrderStatus(String orderId);

    boolean updateDeleteOrderStatus(String userId, String orderId);

    OrderStatusCountsVO getOrderStatusCounts(String userId);

    PagedGridResult getMyOrderTrend(String userId, Integer page, Integer pageSize);
}
