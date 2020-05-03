package com.sg.shopping.controller;

import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.service.ItemsEsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping("items")
public class ItemController {

    @Autowired
    private ItemsEsService itemsEsService;

    @GetMapping("/hello")
    public Object hello() {
        return "Hello World";
    }

    @GetMapping("/es/search")
    @ApiOperation(value = "Search items by keywords", httpMethod = "GET")
    public JsonResult search(
           String keywords,
           String sort,
           Integer page,
           Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 20;
        }

        page--;

        return JsonResult.ok(itemsEsService.searchItems(keywords, sort, page, pageSize));
    }

}
