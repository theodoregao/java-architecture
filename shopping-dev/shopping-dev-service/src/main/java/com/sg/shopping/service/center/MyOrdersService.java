package com.sg.shopping.service.center;

import com.sg.shopping.common.utils.PagedGridResult;

public interface MyOrdersService {

    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);
}
