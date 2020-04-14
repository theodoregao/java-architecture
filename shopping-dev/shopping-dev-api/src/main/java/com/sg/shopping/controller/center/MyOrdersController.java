package com.sg.shopping.controller.center;

import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.controller.BaseController;
import com.sg.shopping.service.center.MyOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "User center my orders related APIs", tags = {"User center my orders related APIs tag"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    @PostMapping("/statusCounts")
    @ApiOperation(value = "Query order status count", httpMethod = "POST")
    public JsonResult statusCount(
            @ApiParam(name = "userId", value = "userId", required = true) @RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg(null);
        }
        return JsonResult.ok(myOrdersService.getOrderStatusCounts(userId));
    }

    @PostMapping("/query")
    @ApiOperation(value = "Query user orders", httpMethod = "POST")
    public JsonResult catItems(
            @ApiParam(name = "userId", value = "userId", required = true) @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "orderStatus", required = false) @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "page", required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "pageSize", required = false) @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        return JsonResult.ok(myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize));
    }

    @ApiOperation(value = "Merchant deliver", notes = "Mimic merchant deliver", httpMethod = "GET")
    @GetMapping("/deliver")
    public JsonResult deliver(
            @ApiParam(name = "orderId", value = "Order Id", required = true)
            @RequestParam String orderId) {
        if (StringUtils.isBlank(orderId)) {
            return JsonResult.errorMsg("Order id cannot be null");
        }
        return JsonResult.ok(myOrdersService.updateDeliverOrderStatus(orderId));
    }

    @ApiOperation(value = "User confirm received", notes = "User confirm received", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public JsonResult confirmReceive(
            @ApiParam(name = "orderId", value = "Order Id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "User Id", required = true)
            @RequestParam String userId) {
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("Order id cannot be null");
        }

        if (myOrdersService.queryOrders(userId, orderId) == null) {
            return JsonResult.errorMsg("userId and orderId not match");
        }

        return JsonResult.ok(myOrdersService.updateConfirmReceiveOrderStatus(orderId));
    }

    @ApiOperation(value = "Delete order", notes = "Delete order", httpMethod = "POST")
    @PostMapping("/delete")
    public JsonResult delete(
            @ApiParam(name = "orderId", value = "Order Id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "User Id", required = true)
            @RequestParam String userId) {
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("Order id cannot be null");
        }

        if (myOrdersService.queryOrders(userId, orderId) == null) {
            return JsonResult.errorMsg("userId and orderId not match");
        }

        boolean deleteSuccess = myOrdersService.updateDeleteOrderStatus(userId, orderId);

        if (deleteSuccess) {
            return JsonResult.ok();
        } else {
            return JsonResult.errorMsg("delete failed");
        }
    }

    @PostMapping("/trend")
    @ApiOperation(value = "Query user orders trend", httpMethod = "POST")
    public JsonResult trend(
            @ApiParam(name = "userId", value = "userId", required = true) @RequestParam String userId,
            @ApiParam(name = "page", value = "page", required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "pageSize", required = false) @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        return JsonResult.ok(myOrdersService.getMyOrderTrend(userId, page, pageSize));
    }
}
