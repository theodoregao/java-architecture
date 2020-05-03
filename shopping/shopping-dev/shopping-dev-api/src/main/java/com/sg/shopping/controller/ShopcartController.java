package com.sg.shopping.controller;

import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.common.utils.JsonUtils;
import com.sg.shopping.common.utils.RedisOperator;
import com.sg.shopping.pojo.bo.ShopcartBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "Shopping Cart page", tags = {"Shopping Cart page related service"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController extends BaseController {

    final static Logger logger = LoggerFactory.getLogger(ShopcartController.class);

    @Autowired
    private RedisOperator redisOperator;

    @PostMapping("/add")
    @ApiOperation(value = "add", notes = "Add items to shopping cart", httpMethod = "POST")
    public Object add(@RequestParam String userId,
                      @RequestBody ShopcartBO shopcartBO,
                      HttpServletRequest request,
                      HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }

        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopcartBO> list = null;
        if (StringUtils.isNotBlank(shopcartJson)) {
            list = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            boolean isHaving = false;
            for (ShopcartBO sb: list) {
                String tmpSpecId = sb.getSpecId();
                if (tmpSpecId.equals(shopcartBO.getSpecId())) {
                    sb.setBuyCounts(sb.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving)
                list.add(shopcartBO);
        } else {
            list = new ArrayList<>();
            list.add(shopcartBO);
        }

        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(list));

        return JsonResult.ok();
    }

    @PostMapping("/del")
    @ApiOperation(value = "del", notes = "Delete items from shopping cart", httpMethod = "POST")
    public Object delete(@RequestParam String userId,
                      @RequestParam String itemSpecId,
                      HttpServletRequest request,
                      HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JsonResult.errorMsg("");
        }

        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopcartJson)) {
            List<ShopcartBO> list = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            for (ShopcartBO sb: list) {
                String tmpSpecId = sb.getSpecId();
                if (tmpSpecId.equals(itemSpecId)) {
                    list.remove(sb);
                    break;
                }
            }
            redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(list));
        }

        return JsonResult.ok();
    }

}
