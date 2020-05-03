package com.sg.shopping.controller.center;

import com.sg.shopping.common.enums.YesOrNo;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.controller.BaseController;
import com.sg.shopping.pojo.Orders;
import com.sg.shopping.pojo.bo.center.OrderItemsCommentBO;
import com.sg.shopping.service.center.MyCommentsService;
import com.sg.shopping.service.center.MyOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "User center my comments related APIs", tags = {"User center my comments related APIs tag"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    @Autowired
    private MyCommentsService myCommentsService;

    @PostMapping("/pending")
    @ApiOperation(value = "Query user orders", httpMethod = "POST")
    public JsonResult catItems(
            @ApiParam(name = "userId", value = "userId", required = true) @RequestParam String userId,
            @ApiParam(name = "orderId", value = "orderId", required = true) @RequestParam String orderId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(orderId)) {
            return JsonResult.errorMsg(null);
        }

        Orders myOrder = myOrdersService.queryOrders(userId, orderId);
        if (myOrder == null) {
            return JsonResult.errorMsg("userId and orderId not match");
        }

        if (myOrder.getIsComment() == YesOrNo.YES.type) {
            return JsonResult.errorMsg("The order has already commented.");
        }

        return JsonResult.ok(myCommentsService.queryPendingComment(orderId));
    }

    @PostMapping("/saveList")
    @ApiOperation(value = "Save comments", notes = "Save comments", httpMethod = "POST")
    public JsonResult catItems(
            @ApiParam(name = "userId", value = "userId", required = true) @RequestParam String userId,
            @ApiParam(name = "orderId", value = "orderId", required = true) @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(orderId)) {
            return JsonResult.errorMsg(null);
        }

        Orders myOrder = myOrdersService.queryOrders(userId, orderId);
        if (myOrder == null) {
            return JsonResult.errorMsg("userId and orderId not match");
        }

        if (commentList == null || commentList.isEmpty()) {
            return JsonResult.errorMsg("comment list cannot be null");
        }

        if (myOrder.getIsComment() == YesOrNo.YES.type) {
            return JsonResult.errorMsg("The order has already commented.");
        }

        myCommentsService.saveComments(orderId, userId, commentList);

        return JsonResult.ok();
    }

    @PostMapping("/query")
    @ApiOperation(value = "Query comments", notes = "Query comments", httpMethod = "POST")
    public JsonResult query(
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

        return JsonResult.ok(myCommentsService.queryMyComments(userId, page, pageSize));
    }
}
