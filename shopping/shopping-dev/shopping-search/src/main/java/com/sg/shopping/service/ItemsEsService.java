package com.sg.shopping.service;

import com.sg.shopping.common.utils.PagedGridResult;

public interface ItemsEsService {
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);
}
