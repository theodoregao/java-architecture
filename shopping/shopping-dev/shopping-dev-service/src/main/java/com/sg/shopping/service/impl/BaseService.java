package com.sg.shopping.service.impl;

import com.github.pagehelper.PageInfo;
import com.sg.shopping.common.utils.PagedGridResult;

import java.util.List;

public class BaseService {

    protected PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage(page);
        pagedGridResult.setRows(list);
        pagedGridResult.setTotal(pageList.getPages());
        pagedGridResult.setRecords(pageList.getTotal());

        return pagedGridResult;
    }
}
