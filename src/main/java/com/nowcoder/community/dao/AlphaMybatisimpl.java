package com.nowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class AlphaMybatisimpl implements AlphaDao{       //bean默认的名字是类名首字母小写
    @Override
    public String select() {
        return "hi Mybatis";
    }
}
