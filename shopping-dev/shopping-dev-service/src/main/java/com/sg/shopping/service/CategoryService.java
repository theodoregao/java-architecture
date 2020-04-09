package com.sg.shopping.service;

import com.sg.shopping.pojo.Category;
import com.sg.shopping.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    List<Category> getRootCategory();

    List<CategoryVO> getSubCategoryList(Integer rootCategoryId);

}
