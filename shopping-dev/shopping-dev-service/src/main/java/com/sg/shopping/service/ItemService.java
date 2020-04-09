package com.sg.shopping.service;

import com.sg.shopping.pojo.Items;
import com.sg.shopping.pojo.ItemsImg;
import com.sg.shopping.pojo.ItemsParam;
import com.sg.shopping.pojo.ItemsSpec;
import com.sg.shopping.pojo.vo.CommentLevelCountsVO;

import java.util.List;

public interface ItemService {
    Items getItemById(String itemId);

    List<ItemsImg> getItemImgList(String itemId);

    List<ItemsSpec> getItemSpecList(String itemId);

    ItemsParam getItemParam(String itemId);

    CommentLevelCountsVO getCommentCounts(String itemId);
}
