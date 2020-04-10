package com.sg.shopping.controller;

import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.pojo.bo.ShopcartBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "Shopping Cart page", tags = {"Shopping Cart page related service"})
@RestController
@RequestMapping("shopcart")
public class ShopcartController {

    final static Logger logger = LoggerFactory.getLogger(ShopcartController.class);

    @PostMapping("/add")
    @ApiOperation(value = "add", notes = "Add items to shopping cart", httpMethod = "POST")
    public Object add(@RequestParam String userId,
                      @RequestBody ShopcartBO shopcartBO,
                      HttpServletRequest request,
                      HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }

        return JsonResult.ok();
    }

}
