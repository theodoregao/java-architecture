package com.sg.shopping.service;

import com.sg.shopping.common.utils.PagedGridResult;
import com.sg.shopping.pojo.Items;
import com.sg.shopping.pojo.ItemsImg;
import com.sg.shopping.pojo.ItemsParam;
import com.sg.shopping.pojo.ItemsSpec;
import com.sg.shopping.pojo.vo.CommentLevelCountsVO;
import com.sg.shopping.pojo.vo.ItemCommentVO;
import com.sg.shopping.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemService {
    Items getItemById(String itemId);

    List<ItemsImg> getItemImgList(String itemId);

    List<ItemsSpec> getItemSpecList(String itemId);

    ItemsParam getItemParam(String itemId);

    CommentLevelCountsVO getCommentCounts(String itemId);

    PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    PagedGridResult searchItemsByCategory(String catId, String sort, Integer page, Integer pageSize);

    List<ShopcartVO> queryItemsBySpecIds(String specIds);

    ItemsSpec queryItemSpecById(String specId);

    String queryItemMainImgById(String itemId);

    void decreaseItemSpecStock(String itemSpecId, int buyCounts);
}
