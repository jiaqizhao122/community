package com.nowcoder.community;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class NewElasticsearchTests {

    @Autowired
    private DiscussPostMapper discussMapper;

    @Autowired
    private DiscussPostRepository discussRepository;
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Test
    public void testInsert() {
        discussRepository.save(discussMapper.selectDiscussPostById(241));
        discussRepository.save(discussMapper.selectDiscussPostById(242));
        discussRepository.save(discussMapper.selectDiscussPostById(243));
    }

    @Test
    public void testInsertList() {
//        discussRepository.saveAll(discussMapper.selectDiscussPost(101, 0, 100,0));
//        discussRepository.saveAll(discussMapper.selectDiscussPost(102, 0, 100));
//        discussRepository.saveAll(discussMapper.selectDiscussPost(103, 0, 100));
//        discussRepository.saveAll(discussMapper.selectDiscussPost(111, 0, 100));
//        discussRepository.saveAll(discussMapper.selectDiscussPost(112, 0, 100));
//        discussRepository.saveAll(discussMapper.selectDiscussPost(131, 0, 100));
//        discussRepository.saveAll(discussMapper.selectDiscussPost(132, 0, 100));
//        discussRepository.saveAll(discussMapper.selectDiscussPost(133, 0, 100));
//        discussRepository.saveAll(discussMapper.selectDiscussPost(134, 0, 100));
    }

    @Test
    public void testUpdate() {
        DiscussPost post = discussMapper.selectDiscussPostById(231);
        post.setContent("lalala");
        discussRepository.save(post);
    }

    @Test
    public void testDelete() {
        // discussRepository.deleteById(231);
        discussRepository.deleteAll();
    }

//    @Test
//    public void testSearchByRepository() {
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
//                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
//                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
//                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
//                .withPageable(PageRequest.of(0, 10))
//                .withHighlightFields(
//                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
//                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
//                ).build();
//
//        // elasticTemplate.queryForPage(searchQuery, class, SearchResultMapper)
//        // 底层获取得到了高亮显示的值, 但是没有返回.
//
//        Page<DiscussPost> page = discussRepository.search(searchQuery);
//        System.out.println(page.getTotalElements());
//        System.out.println(page.getTotalPages());
//        System.out.println(page.getNumber());
//        System.out.println(page.getSize());
//        for (DiscussPost post : page) {
//            System.out.println(post);
//        }
//    }

    @Test
    public void testSearchByTemplate() throws IOException {
        SearchRequest searchRequest = SearchRequest.of(builder -> builder
                .query(q -> q.multiMatch(m -> m
                        .query("互联网寒冬").fields("title", "content"))
                )
                .sort(s -> s.field(f -> f.field("type").order(SortOrder.Desc)))
                .sort(s -> s.field(f -> f.field("score").order(SortOrder.Desc)))
                .sort(s -> s.field(f -> f.field("createTime").order(SortOrder.Desc)))
                .from(0)
                .size(10)
                .highlight(h -> h
                        .fields("title", f -> f.preTags("<em>").postTags("</em>"))
                        .fields("content", f -> f.preTags("<em>").postTags("</em>"))
                )
        );

        SearchResponse<DiscussPost> response = elasticsearchClient.search(searchRequest, DiscussPost.class);  //执行搜索请求，返回的结果被映射为DiscussPost类的实例

        List<DiscussPost> posts = new ArrayList<>();
        for (Hit<DiscussPost> hit : response.hits().hits()) {     //通过循环遍历响应中的每个命中(hit)
            // 提取出搜索结果并根据需要处理高亮字段
            DiscussPost post = hit.source();     //只需一步！
            if (hit.highlight().containsKey("title")) {
                List<String> titleHighlight = hit.highlight().get("title");
                assert post != null;
                post.setTitle(String.join(" ", titleHighlight));
            }
            if (hit.highlight().containsKey("content")) {
                List<String> contentHighlight = hit.highlight().get("content");
                assert post != null;
                post.setContent(String.join(" ", contentHighlight));
            }
            posts.add(post);
        }
        Page<DiscussPost> page = new PageImpl<>(posts, PageRequest.of(0, 10),
                response.hits().total().value());  //总命中数

        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        for (DiscussPost post : page) {
            System.out.println(post);
        }
    }
}
