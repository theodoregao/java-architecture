package com.sg.shopping.mapper;

import com.sg.shopping.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryMapperCustom {

    List<CategoryVO> getSubCategoryList(Integer rootCatId);

}