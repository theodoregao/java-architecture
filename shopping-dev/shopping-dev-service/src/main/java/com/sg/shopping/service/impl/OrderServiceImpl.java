package com.sg.shopping.service.impl;

import com.sg.shopping.common.enums.OrderStatusEnum;
import com.sg.shopping.common.enums.YesOrNo;
import com.sg.shopping.common.utils.DateUtil;
import com.sg.shopping.mapper.OrderItemsMapper;
import com.sg.shopping.mapper.OrderStatusMapper;
import com.sg.shopping.mapper.OrdersMapper;
import com.sg.shopping.pojo.*;
import com.sg.shopping.pojo.bo.ShopcartBO;
import com.sg.shopping.pojo.bo.SubmitOrderBO;
import com.sg.shopping.pojo.vo.MerchantOrdersVO;
import com.sg.shopping.pojo.vo.OrderVO;
import com.sg.shopping.service.AddressService;
import com.sg.shopping.service.ItemService;
import com.sg.shopping.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderVO createOrder(List<ShopcartBO> shopcartList, SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        Integer postAmount = 0;

        String orderId = Sid.next();
        UserAddress address = addressService.queryUserAddress(userId, addressId);

        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);

        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince() +
                " " + address.getCity() +
                " " + address.getDistrict() +
                " " + address.getDetail());
        newOrder.setPostAmount(postAmount);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());

        String[] specIds = itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realPayAmount = 0;
        List<ShopcartBO> toBeRemovedShopcartList = new ArrayList<>();
        for (String specId: specIds) {
            ShopcartBO cartItem = getBuyCountsFromShopcart(shopcartList, specId);
            int buyCounts = cartItem.getBuyCounts();
            toBeRemovedShopcartList.add(cartItem);

            ItemsSpec itemsSpec = itemService.queryItemSpecById(specId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            String itemId = itemsSpec.getItemId();
            Items item = itemService.getItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(sid.nextShort());
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecId(specId);;
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());

            itemService.decreaseItemSpecStock(specId, buyCounts);

            orderItemsMapper.insert(subOrderItem);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);

        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        orderVO.setToBeRemovedShopcartList(toBeRemovedShopcartList);

        return orderVO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void closeOrder() {
        OrderStatus queryOrderSatus = new OrderStatus();
        queryOrderSatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> orderStatus = orderStatusMapper.select(queryOrderSatus);
        for (OrderStatus os: orderStatus) {
            Date createdDateTime = os.getCreatedTime();
            int days = DateUtil.daysBetween(createdDateTime, new Date());
            if (days >= 1) {
                doCloseOrder(os.getOrderId());
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    private void doCloseOrder(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatus.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKey(orderStatus);
    }

    private ShopcartBO getBuyCountsFromShopcart(List<ShopcartBO> shopcartList, String specId) {
        for (ShopcartBO cart : shopcartList) {
            if (cart.getSpecId().equals(specId)) {
                return cart;
            }
        }
        return null;
    }
}
