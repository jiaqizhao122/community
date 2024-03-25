package com.nowcoder.community.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElasticSearchService {
    @Autowired
    private DiscussPostRepository discussRepository;
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public void saveDiscussPost(DiscussPost post) {
        discussRepository.save(post);    //再save一次就是删除
    }

    public void deleteDiscussPost(int id) {
        discussRepository.deleteById(id);
    }

    public Page<DiscussPost> searchDiscussPost(String keyword, int current, int limit) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(builder -> builder
                .query(q -> q.multiMatch(m -> m
                        .query(keyword).fields("title", "content"))
                )
                .sort(s -> s.field(f -> f.field("type").order(SortOrder.Desc)))
                .sort(s -> s.field(f -> f.field("score").order(SortOrder.Desc)))
                .sort(s -> s.field(f -> f.field("createTime").order(SortOrder.Desc)))
                .from(current)
                .size(limit)
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
        Page<DiscussPost> page = new PageImpl<>(posts, PageRequest.of(current, limit),
                response.hits().total().value());  //总命中数
        return page;

    }


}
