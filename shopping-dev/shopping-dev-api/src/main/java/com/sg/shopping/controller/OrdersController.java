package com.sg.shopping.controller;

import com.sg.shopping.common.enums.PayMethod;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.pojo.bo.SubmitOrderBO;
import com.sg.shopping.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "Order related APIs", tags = {"Order related APIs"})
@RestController
@RequestMapping("orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @ApiOperation(value = "create", notes = "Create a new order", httpMethod = "POST")
    public JsonResult create(@RequestBody SubmitOrderBO submitOrderBO) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            JsonResult.errorMsg("Not supported pay method");
        }

        orderService.createOrder(submitOrderBO);

        return JsonResult.ok();
    }
}
