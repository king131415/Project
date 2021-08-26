package org.example.service.ES;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.example.Mapper.ES.DiscusspostRepository;
import org.example.Model.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EsService {

    @Autowired
    private DiscusspostRepository postRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticTemplate;

    //向ES服务器存数据
    public void sevaDiscusspost(DiscussPost post){
        postRepository.save(post);
    }
    //控制ES服务器删除数据
    public void deleteDscuccPost(int id){
        postRepository.deleteById(id);
    }

    //搜索方法
    public Map<String,Object> searchDiscussPost(String value,int current,int limit){
        //构建搜索条件 NativeSearchQuery 是实现构建搜索条件的 withQuery是构建搜索条件的
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(value, "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime.keyword").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        // elasticTemplate.queryForPage(searchQuery, class, SearchResultMapper)
        // 底层获取得到了高亮显示的值, 但是没有返回.
        SearchHits<DiscussPost> search =elasticTemplate.search(searchQuery, DiscussPost.class);
        //得到查询返回的内容
        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();

        //统计总数
        int totalHits =(int)search.getTotalHits();
        int count= (int) totalHits;
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("count",count);

        //设置一个最后需要返回的实体类集合
        List<DiscussPost> posts = new ArrayList<>();
        //遍历返回的内容进行处理
        for(SearchHit<DiscussPost> searchHit:searchHits){
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中      highlightFields.get("title").get(0)可能匹配多个，取第一个就行了
            searchHit.getContent().setTitle(highlightFields.get("title")==null ? searchHit.getContent().getTitle():highlightFields.get("title").get(0));
            searchHit.getContent().setContent(highlightFields.get("content")==null ? searchHit.getContent().getContent():highlightFields.get("content").get(0));
//
//            String s = searchHit.getContent().getCreateTime().toString();
//            searchHit.getContent().setCreateTime(new Date(Long.valueOf(s)));

            //放到实体类中
            posts.add(searchHit.getContent());
        }
        resultMap.put("posts",posts);
        return resultMap;

    }
}
