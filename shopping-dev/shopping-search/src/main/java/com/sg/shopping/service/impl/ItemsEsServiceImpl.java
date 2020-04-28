package com.sg.shopping.service.impl;

import com.sg.shopping.common.utils.PagedGridResult;
import com.sg.shopping.es.pojo.Items;
import com.sg.shopping.service.ItemsEsService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemsEsServiceImpl implements ItemsEsService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);
        String itemNameField = "itemName";
        SearchQuery query =
                new NativeSearchQueryBuilder()
                        .withQuery(QueryBuilders.matchQuery(itemNameField, keywords))
                        .withHighlightFields(new HighlightBuilder.Field(itemNameField)
                                .preTags("<font color='red'>")
                                .postTags("</font>"))
                        .withPageable(pageable)
                        .build();
        AggregatedPage<Items> pagedItems = elasticsearchTemplate.queryForPage(query, Items.class, new SearchResultMapper() {

            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<Items> itemList = new ArrayList<>();
                SearchHits searchHits = searchResponse.getHits();
                for (SearchHit searchHit: searchHits) {
                    HighlightField highlightField = searchHit.getHighlightFields().get(itemNameField);
                    String itemName = highlightField.getFragments()[0].toString();
                    String itemId = (String) searchHit.getSourceAsMap().get("itemId");
                    String imgUrl = (String) searchHit.getSourceAsMap().get("imgUrl");
                    Integer price = (Integer) searchHit.getSourceAsMap().get("price");
                    Integer sellcounts = (Integer) searchHit.getSourceAsMap().get("sellCounts");
                    Items item = new Items();
                    item.setItemName(itemName);
                    item.setItemId(itemId);
                    item.setImgUrl(imgUrl);
                    item.setPrice(price);
                    item.setSellCounts(sellcounts);
                    itemList.add(item);
                }

                if (itemList.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>)itemList, pageable, searchResponse.getHits().totalHits);
                }

                return null;
            }
        });
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(pagedItems.getContent());
        gridResult.setPage(page + 1);
        gridResult.setTotal(pagedItems.getTotalPages());
        gridResult.setRecords(pagedItems.getTotalElements());
        return gridResult;
    }
}
