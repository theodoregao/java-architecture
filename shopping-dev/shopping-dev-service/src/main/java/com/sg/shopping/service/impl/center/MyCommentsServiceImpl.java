package com.sg.shopping.service.impl.center;

import com.sg.shopping.mapper.OrderItemsMapper;
import com.sg.shopping.mapper.UserInfoMapper;
import com.sg.shopping.pojo.OrderItems;
import com.sg.shopping.service.center.MyCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyCommentsServiceImpl implements MyCommentsService {

    @Autowired
    OrderItemsMapper orderItemsMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);
        return orderItemsMapper.select(orderItems);
    }
}
