package com.sg.shopping.controller;

import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.pojo.Items;
import com.sg.shopping.pojo.ItemsImg;
import com.sg.shopping.pojo.ItemsParam;
import com.sg.shopping.pojo.ItemsSpec;
import com.sg.shopping.pojo.vo.ItemInfoVO;
import com.sg.shopping.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "Item page", tags = {"Item page related service"})
@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/info/{itemId}")
    @ApiOperation(value = "Get item info by item id", httpMethod = "GET")
    public JsonResult info(@ApiParam(name = "itemId", value = "itemId", required = true)
                           @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        Items item = itemService.getItemById(itemId);
        List<ItemsImg> itemImgList = itemService.getItemImgList(itemId);
        List<ItemsSpec> itemSpecList = itemService.getItemSpecList(itemId);
        ItemsParam itemParams = itemService.getItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemSpecList);
        itemInfoVO.setItemParams(itemParams);

        return JsonResult.ok(itemInfoVO);
    }

    @GetMapping("/commentLevel")
    @ApiOperation(value = "Get item comment level by item id", httpMethod = "GET")
    public JsonResult commentLevel(@ApiParam(name = "itemId", value = "itemId", required = true)
                           @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        return JsonResult.ok(itemService.getCommentCounts(itemId));
    }

}
