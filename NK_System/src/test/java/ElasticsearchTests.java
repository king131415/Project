import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.example.Mapper.DiscussPostMapper;
//import org.example.Mapper.ES.DiscusspostRepository;
import org.example.Mapper.ES.DiscusspostRepository;
import org.example.Model.DiscussPost;
import org.example.NkApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NkApplication.class)
public class ElasticsearchTests {

    @Autowired
    private DiscussPostMapper discussMapper;

    @Autowired
    private DiscusspostRepository discussRepository;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ElasticsearchRestTemplate  elasticTemplate;

//    @Autowired
//    private ElasticsearchTemplate elasticTemplate;

//    private ElasticsearchTemplate elasticTemplate;

    @Test
    public void testInsert() {
        DiscussPost discussPost = discussMapper.selectDiscussPostById(241);


        DiscussPost save = discussRepository.save(discussPost);
        System.out.println(save);
//        discussRepository.save(discussMapper.selectDiscussPostById(242));
//        discussRepository.save(discussMapper.selectDiscussPostById(243));
    }

    @Test
    public void testUpdate() {
        DiscussPost post = discussMapper.selectDiscussPostById(241);
        post.setContent("????????????,????????????.");
        discussRepository.save(post);
    }
    @Test
    public void testFind() {
        // discussRepository.deleteById(231);
        Optional<DiscussPost> byId = discussRepository.findById(241);
        DiscussPost discussPost = byId.get();
        System.out.println(discussPost);

    }

    @Test
    public void testDelete() {
        // discussRepository.deleteById(231);
        discussRepository.deleteAll();
    }

    @Test
    public void testInsertList() {
        discussRepository.saveAll(discussMapper.selectDiscussPosts(101, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(102, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(103, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(111, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(112, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(131, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(132, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(133, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(134, 0, 100));
    }

    @Test
    public void testSearchByRepository() {

        //?????????????????? NativeSearchQuery ?????????????????????????????? withQuery????????????????????????
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("???????????????", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

//        // elasticTemplate.queryForPage(searchQuery, class, SearchResultMapper)
//        // ???????????????????????????????????????, ??????????????????.
//        SearchHits<DiscussPost> search =elasticTemplate.search(searchQuery, DiscussPost.class);
//        //???????????????????????????
//        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
//
//        //????????????????????????????????????????????????
//        List<DiscussPost> users = new ArrayList<>();
//        //?????????????????????????????????
//        for(SearchHit<DiscussPost> searchHit:searchHits){
//            //???????????????
//            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
//            //???????????????????????????content???      highlightFields.get("title").get(0)??????????????????????????????????????????
//            searchHit.getContent().setTitle(highlightFields.get("title")==null ? searchHit.getContent().getTitle():highlightFields.get("title").get(0));
//            searchHit.getContent().setContent(highlightFields.get("content")==null ? searchHit.getContent().getContent():highlightFields.get("content").get(0));
//            //??????????????????
//            users.add(searchHit.getContent());
//        }
//
//        for(DiscussPost discussPost:users){
//            System.out.println(discussPost);
//        }



        Page<DiscussPost> page = discussRepository.search(searchQuery);
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        for (DiscussPost post : page) {
            System.out.println(post);
        }


    }

}
