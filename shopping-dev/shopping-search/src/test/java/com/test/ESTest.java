package com.test;

import com.sg.shopping.Application;
import com.sg.shopping.es.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    // 不建议使用代码对索引进行管理
//    @Test
    public void createIndexStu() {
        IndexQuery indexQuery = null;
        indexQuery = new IndexQueryBuilder().withObject(
                generateStu(1001L,
                        "Harry Porter",
                        128,
                        1.1f,
                        "Harry Porter",
                        "I am the magic"))
                .build();
        elasticsearchTemplate.index(indexQuery);
        indexQuery = new IndexQueryBuilder().withObject(
                generateStu(1002L,
                        "Spider Man",
                        33,
                        100.1f,
                        "Spider Man",
                        "I save the world"))
                .build();
        elasticsearchTemplate.index(indexQuery);
        indexQuery = new IndexQueryBuilder().withObject(
                generateStu(1003L,
                        "Bat Man",
                        28,
                        123.1f,
                        "Batty Man",
                        "I save the world also"))
                .build();
        elasticsearchTemplate.index(indexQuery);
        indexQuery = new IndexQueryBuilder().withObject(
                generateStu(1004L,
                        "Ant Man",
                        18,
                        21.7f,
                        "Ant ant ant",
                        "I didn't not save the world"))
                .build();
        elasticsearchTemplate.index(indexQuery);
        indexQuery = new IndexQueryBuilder().withObject(
                generateStu(1005L,
                        "Normal man",
                        33,
                        6.1f,
                        "Normal people",
                        "I know nothing about magic, and will not save the world"))
                .build();
        elasticsearchTemplate.index(indexQuery);
    }

    private Stu generateStu(Long id, String name, Integer age, Float money, String sign, String description) {
        Stu stu = new Stu();
        stu.setStuId(id);
        stu.setName(name);
        stu.setAge(age);
        stu.setMoney(money);
        stu.setSign(sign);
        stu.setDescription(description);
        return stu;
    }

//    @Test
    public void deleteIndexStu() {
        elasticsearchTemplate.deleteIndex(Stu.class);
    }

//    @Test
    public void updateStuDoc() {
        Map<String, Object> map = new HashMap<>();
        map.put("sign", "I am not Harry Porter");
        map.put("money", 99.9f);
        map.put("age", 128);
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(map);
        UpdateQuery updateQuery = new UpdateQueryBuilder().withClass(Stu.class)
                .withId("1001")
                .withIndexRequest(indexRequest).build();
        elasticsearchTemplate.update(updateQuery);
    }

//    @Test
    public void queryStuDoc() {
        GetQuery getQuery = new GetQuery();
        getQuery.setId("1001");
        Stu stu = elasticsearchTemplate.queryForObject(getQuery, Stu.class);
        System.out.println(stu);
    }

//    @Test
    public void deleteStuDoc() {
        elasticsearchTemplate.delete(Stu.class, "1001");
    }

//    @Test
    public void searchStuDoc() {
        Pageable pageable = PageRequest.of(0, 3);
        SearchQuery query =
                new NativeSearchQueryBuilder()
                        .withQuery(QueryBuilders.matchQuery("description", "save world"))
                        .withPageable(pageable)
                        .build();
        AggregatedPage<Stu> pagedStu = elasticsearchTemplate.queryForPage(query, Stu.class);
        System.out.print(pagedStu.getTotalPages());
        List<Stu> stuList = pagedStu.getContent();
        for (Stu stu: stuList) {
            System.out.println(stu);
        }
    }

//    @Test
    public void highlightStuDoc() {
        Pageable pageable = PageRequest.of(0, 3);
        SortBuilder ageSortBuilder = new FieldSortBuilder("age")
                .order(SortOrder.ASC);
        SortBuilder moneySortBuilder = new FieldSortBuilder("money")
                .order(SortOrder.ASC);
        SearchQuery query =
                new NativeSearchQueryBuilder()
                        .withQuery(QueryBuilders.matchQuery("description", "save world"))
                        .withHighlightFields(new HighlightBuilder.Field("description")
                                .preTags("<font color='red'>")
                                .postTags("</font>"))
                        .withPageable(pageable)
                        .withSort(ageSortBuilder)
                        .withSort(moneySortBuilder)
                        .build();
        AggregatedPage<Stu> pagedStu = elasticsearchTemplate.queryForPage(query, Stu.class, new SearchResultMapper() {

            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<Stu> stuList = new ArrayList<>();
                SearchHits searchHits = searchResponse.getHits();
                for (SearchHit searchHit: searchHits) {
                    HighlightField highlightField = searchHit.getHighlightFields().get("description");
                    String description = highlightField.getFragments()[0].toString();
                    Object stuId = searchHit.getSourceAsMap().get("stuId");
                    String name = (String) searchHit.getSourceAsMap().get("name");
                    Integer age = (Integer) searchHit.getSourceAsMap().get("age");
                    String sign = (String) searchHit.getSourceAsMap().get("sign");
                    Object money = searchHit.getSourceAsMap().get("money");
                    Stu stu = new Stu();
                    stu.setDescription(description);
                    stu.setStuId(Long.valueOf(stuId.toString()));
                    stu.setName(name);
                    stu.setAge(age);
                    stu.setSign(sign);
                    stu.setMoney(Float.valueOf(money.toString()));
                    stuList.add(stu);
                }

                if (stuList.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>)stuList);
                }

                return null;
            }
        });
        System.out.print(pagedStu.getTotalPages());
        List<Stu> stuList = pagedStu.getContent();
        for (Stu stu: stuList) {
            System.out.println(stu);
        }
    }

}
