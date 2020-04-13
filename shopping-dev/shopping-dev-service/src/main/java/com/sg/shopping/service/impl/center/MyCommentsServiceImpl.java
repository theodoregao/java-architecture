package com.sg.shopping.service.impl.center;

import com.sg.shopping.common.enums.YesOrNo;
import com.sg.shopping.mapper.*;
import com.sg.shopping.pojo.OrderItems;
import com.sg.shopping.pojo.OrderStatus;
import com.sg.shopping.pojo.Orders;
import com.sg.shopping.pojo.bo.center.OrderItemsCommentBO;
import com.sg.shopping.service.center.MyCommentsService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyCommentsServiceImpl implements MyCommentsService {

    @Autowired
    Sid sid;

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    OrderItemsMapper orderItemsMapper;

    @Autowired
    ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    OrderStatusMapper orderStatusMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);
        return orderItemsMapper.select(orderItems);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> orderItemsCommentBOS) {
        for (OrderItemsCommentBO oic : orderItemsCommentBOS) {
            oic.setCommentId(sid.nextShort());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("commentList", orderItemsCommentBOS);
        itemsCommentsMapperCustom.saveComments(map);

        Orders order = new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNo.YES.type);
        order.setUpdatedTime(new Date());
        ordersMapper.updateByPrimaryKeySelective(order);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }
}
