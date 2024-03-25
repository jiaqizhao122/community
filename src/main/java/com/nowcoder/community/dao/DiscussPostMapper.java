package com.nowcoder.community.dao;

import com.jhlabs.image.DissolveFilter;
import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//打上mapper注解以后，才能被容器扫描到
@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPost(int userId, int offset, int limit, int orderMode);

    //@Param注解用于给参数起别名
    //如果只有一个参数，并且在<if>里使用，则必须要加别名，<if>用于动态标签。
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);

    int updateScore(int id, double score);

}
