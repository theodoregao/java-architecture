package com.sg.shopping.service.impl;

import com.sg.shopping.mapper.*;
import com.sg.shopping.pojo.*;
import com.sg.shopping.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items getItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public List<ItemsImg> getItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        example.createCriteria().andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Override
    public List<ItemsSpec> getItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
         example.createCriteria().andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Override
    public ItemsParam getItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        example.createCriteria().andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }
}
