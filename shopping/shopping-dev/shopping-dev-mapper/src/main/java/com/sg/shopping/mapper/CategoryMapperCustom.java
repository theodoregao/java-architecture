package com.sg.shopping.mapper;

import com.sg.shopping.pojo.vo.CategoryVO;
import com.sg.shopping.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {

    List<CategoryVO> getSubCategoryList(Integer rootCatId);

    List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);

}