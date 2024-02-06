package com.nowcoder.community.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository("alphaHibernate")
public class AlphaDaohibernatelmpl implements AlphaDao{
    @Override
    public String select() {
        return "hi";
    }
}
