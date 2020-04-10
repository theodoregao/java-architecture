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
public class ItemsController extends BaseController {

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
    @ApiOperation(value = "Get item comment level count by item id", httpMethod = "GET")
    public JsonResult commentLevel(@ApiParam(name = "itemId", value = "itemId", required = true)
                                   @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        return JsonResult.ok(itemService.getCommentCounts(itemId));
    }

    @GetMapping("/comments")
    @ApiOperation(value = "Get paged item comment by item id", httpMethod = "GET")
    public JsonResult comments(
            @ApiParam(name = "itemId", value = "itemId", required = true) @RequestParam String itemId,
            @ApiParam(name = "level", value = "level", required = false) @RequestParam Integer level,
            @ApiParam(name = "page", value = "page", required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "pageSize", required = false) @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = BaseController.COMMENT_PAGE_SIZE;
        }

        return JsonResult.ok(itemService.queryPagedComments(itemId, level, page, pageSize));
    }

    @GetMapping("/search")
    @ApiOperation(value = "Search items by keywords", httpMethod = "GET")
    public JsonResult search(
            @ApiParam(name = "keywords", value = "keywords", required = true) @RequestParam String keywords,
            @ApiParam(name = "sort", value = "sort", required = false) @RequestParam String sort,
            @ApiParam(name = "page", value = "page", required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "pageSize", required = false) @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = BaseController.PAGE_SIZE;
        }

        return JsonResult.ok(itemService.searchItems(keywords, sort, page, pageSize));
    }

    @GetMapping("/catItems")
    @ApiOperation(value = "Search items by category", httpMethod = "GET")
    public JsonResult catItems(
            @ApiParam(name = "catId", value = "catId", required = true) @RequestParam String catId,
            @ApiParam(name = "sort", value = "sort", required = false) @RequestParam String sort,
            @ApiParam(name = "page", value = "page", required = false) @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "pageSize", required = false) @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(catId)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = BaseController.PAGE_SIZE;
        }

        return JsonResult.ok(itemService.searchItemsByCategory(catId, sort, page, pageSize));
    }

    @GetMapping("/refresh")
    @ApiOperation(value = "Refresh shopping cart data", httpMethod = "GET")
    public JsonResult refresh(
            @ApiParam(name = "itemSpecIds", value = "itemSpecIds", required = true) @RequestParam String itemSpecIds) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return JsonResult.ok();
        }

        return JsonResult.ok(itemService.queryItemsBySpecIds(itemSpecIds));
    }

}
