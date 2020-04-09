package com.sg.shopping.service.impl;

import com.sg.shopping.mapper.CarouselMapper;
import com.sg.shopping.pojo.Carousel;
import com.sg.shopping.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> getAllCarouse(Integer isShow) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);

        return carouselMapper.selectByExample(example);
    }
}
