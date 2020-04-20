package com.sg.shopping.controller;

import com.sg.shopping.common.enums.OrderStatusEnum;
import com.sg.shopping.common.enums.PayMethod;
import com.sg.shopping.common.utils.CookieUtils;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.common.utils.JsonUtils;
import com.sg.shopping.common.utils.RedisOperator;
import com.sg.shopping.pojo.bo.ShopcartBO;
import com.sg.shopping.pojo.bo.SubmitOrderBO;
import com.sg.shopping.pojo.vo.MerchantOrdersVO;
import com.sg.shopping.pojo.vo.OrderVO;
import com.sg.shopping.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "Order related APIs", tags = {"Order related APIs"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @PostMapping("/create")
    @ApiOperation(value = "create", notes = "Create a new order", httpMethod = "POST")
    public JsonResult create(@RequestBody SubmitOrderBO submitOrderBO,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            JsonResult.errorMsg("Not supported pay method");
        }

        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());
        if (StringUtils.isBlank(shopcartJson)) {
            return JsonResult.errorMsg("shopping cart data incorrect");
        }

        List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);

        OrderVO orderVO = orderService.createOrder(shopcartList, submitOrderBO);
        String orderId = orderVO.getOrderId();

        shopcartList.removeAll(orderVO.getToBeRemovedShopcartList());
        redisOperator.set(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId(), JsonUtils.objectToJson(shopcartList));
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartList), true);

        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(PAY_RETURN_URL);
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", IMOOC_USERID);
        headers.add("password", IMOOC_PASSCODE);

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<JsonResult> responseEntity = restTemplate.postForEntity(PAYMENT_URL, entity, JsonResult.class);

        JsonResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != HttpStatus.OK.value()) {
            return JsonResult.errorMsg("支付中心失败");
        }

        return JsonResult.ok(orderId);
    }

    @PostMapping("updateCode")
    @ApiOperation(value = "Update code", notes = "Update code for return url", httpMethod = "POST")
    public JsonResult updateCode(@RequestParam String code) {
        updateReturnUrl(code);
        return JsonResult.ok();
    }

    @PostMapping("notifyPaid")
    public Integer notifyPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public JsonResult getPaidOrderInfo(String orderId) {
        return JsonResult.ok(orderService.queryOrderStatusInfo(orderId));
    }
}
