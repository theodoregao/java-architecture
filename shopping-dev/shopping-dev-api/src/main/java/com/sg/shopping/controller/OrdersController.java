package com.sg.shopping.controller;

import com.sg.shopping.common.enums.OrderStatusEnum;
import com.sg.shopping.common.enums.PayMethod;
import com.sg.shopping.common.utils.CookieUtils;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.pojo.bo.SubmitOrderBO;
import com.sg.shopping.pojo.vo.OrderVO;
import com.sg.shopping.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "Order related APIs", tags = {"Order related APIs"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @ApiOperation(value = "create", notes = "Create a new order", httpMethod = "POST")
    public JsonResult create(@RequestBody SubmitOrderBO submitOrderBO,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            JsonResult.errorMsg("Not supported pay method");
        }

        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        orderVO.getMerchantOrdersVO().setReturnUrl(PAY_RETURN_URL);

//        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);

        return JsonResult.ok(orderVO);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }
}