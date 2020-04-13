package com.sg.shopping.service.center;

import com.sg.shopping.pojo.OrderItems;
import com.sg.shopping.pojo.UserInfo;

import java.util.List;

public interface MyCommentsService {

    List<OrderItems> queryPendingComment(String orderId);
}
