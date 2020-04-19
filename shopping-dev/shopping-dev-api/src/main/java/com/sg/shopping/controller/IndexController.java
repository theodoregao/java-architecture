package com.sg.shopping.controller;

import com.sg.shopping.common.enums.YesOrNo;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.common.utils.JsonUtils;
import com.sg.shopping.common.utils.RedisOperator;
import com.sg.shopping.pojo.Carousel;
import com.sg.shopping.pojo.Category;
import com.sg.shopping.pojo.vo.CategoryVO;
import com.sg.shopping.service.CarouselService;
import com.sg.shopping.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Api(value = "Index page", tags = {"Index page related service"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/carousel")
    @ApiOperation(value = "Get all the carousel data", httpMethod = "GET")
    public JsonResult carousel() {
        List<Carousel> list;
        String carouseStr =  redisOperator.get("carousel");
        if (StringUtils.isBlank(carouseStr)) {
            list = carouselService.getAllCarouse((YesOrNo.YES.type));
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(carouseStr, Carousel.class);
        }
        return JsonResult.ok(list);
    }

    @GetMapping("/cats")
    @ApiOperation(value = "Get the first level category", httpMethod = "GET")
    public JsonResult cats() {
        List<Category> list;
        String catStr = redisOperator.get("cats");
        if (StringUtils.isBlank(catStr)) {
            list = categoryService.getRootCategory();
            redisOperator.set("cat", JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(catStr, Category.class);
        }

        return JsonResult.ok(list);
    }

    @GetMapping("/subCat/{rootCategoryId}")
    @ApiOperation(value = "Get sub category", httpMethod = "GET")
    public JsonResult subCat(
            @ApiParam(name="rootCategoryId", value = "root level category id", required = true)
            @PathVariable Integer rootCategoryId) {
        if (rootCategoryId == null) {
            return JsonResult.errorMsg("");
        }

        String subCatKey = "sub_cat_" + rootCategoryId;
        List<CategoryVO> list = new ArrayList<>();
        String catsStr = redisOperator.get(subCatKey);
        if (StringUtils.isBlank(catsStr)) {
            list = categoryService.getSubCategoryList(rootCategoryId);
            redisOperator.set(subCatKey, JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(catsStr, CategoryVO.class);
        }
        return JsonResult.ok(list);
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
