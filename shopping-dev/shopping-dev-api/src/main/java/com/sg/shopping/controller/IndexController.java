package com.sg.shopping.controller;

import com.sg.shopping.common.enums.YesOrNo;
import com.sg.shopping.common.utils.JsonResult;
import com.sg.shopping.pojo.Carousel;
import com.sg.shopping.service.CarouselService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(value = "Index page", tags = {"Index page related service"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping("/carousel")
    @ApiOperation(value = "Get all the carousel data", httpMethod = "GET")
    public JsonResult carousel() {
        return JsonResult.ok(carouselService.getAllCarouse((YesOrNo.YES.type)));
    }

}
