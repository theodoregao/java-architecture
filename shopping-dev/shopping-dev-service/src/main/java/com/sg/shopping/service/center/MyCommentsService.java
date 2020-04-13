package com.sg.shopping.service.center;

import com.sg.shopping.pojo.OrderItems;
import com.sg.shopping.pojo.UserInfo;
import com.sg.shopping.pojo.bo.center.OrderItemsCommentBO;

import java.util.List;

public interface MyCommentsService {

    List<OrderItems> queryPendingComment(String orderId);

    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> orderItemsCommentBOS);
}
