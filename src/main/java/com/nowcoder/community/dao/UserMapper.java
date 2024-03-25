package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    //可以使用repository来让容器装配这个bean,但是mybatis可以使用Mapper来标识这个bean
    //需要在这里面声明一些增删改查的方法，再写上sql的配置文件
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id,int status);

    int updateHeader(int id,String headerUrl);

    int updatePassword(int id,String password);


}
