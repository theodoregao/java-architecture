package com.sg.shopping.controller;

import com.sg.shopping.common.enums.YesOrNo;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.service.CarouselService;
import com.sg.shopping.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "Index page", tags = {"Index page related service"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/carousel")
    @ApiOperation(value = "Get all the carousel data", httpMethod = "GET")
    public JsonResult carousel() {
        return JsonResult.ok(carouselService.getAllCarouse((YesOrNo.YES.type)));
    }

    @GetMapping("/cats")
    @ApiOperation(value = "Get the first level category", httpMethod = "GET")
    public JsonResult cats() {
        return JsonResult.ok(categoryService.getRootCategory());
    }

    @GetMapping("/subCat/{rootCategoryId}")
    @ApiOperation(value = "Get sub category", httpMethod = "GET")
    public JsonResult subCat(
            @ApiParam(name="rootCategoryId", value = "root level category id", required = true)
            @PathVariable Integer rootCategoryId) {
        if (rootCategoryId == null) {
            return JsonResult.errorMsg("");
        }
        return JsonResult.ok(categoryService.getSubCategoryList(rootCategoryId));
    }

    @GetMapping("/sixNewItems/{rootCategoryId}")
    @ApiOperation(value = "Get next 6 new items", httpMethod = "GET")
    public JsonResult sixNewItems(
            @ApiParam(name="rootCategoryId", value = "root level category id", required = true)
            @PathVariable Integer rootCategoryId) {
        if (rootCategoryId == null) {
            return JsonResult.errorMsg("");
        }
        return JsonResult.ok(categoryService.getSixNewItemsLazy(rootCategoryId));
    }

}
