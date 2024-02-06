package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//打上mapper注解以后，才能被容器扫描到
@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPost(int userId, int offset, int limit);

    //@Param注解用于给参数起别名
    //如果只有一个参数，并且在<if>里使用，则必须要加别名，<if>用于动态标签。
    int selectDiscussPostRows(@Param("userId") int userId);


}
