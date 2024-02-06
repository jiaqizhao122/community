package com.nowcoder.community.service;


import com.nowcoder.community.dao.AlphaDao;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("实例化化AlphaService，这是一个构造器");
    }

    @PostConstruct    //这个方法会在构造器之后调用，可以用来初始化某些数据
    public void init() {
        System.out.println("初始化AlphaService");
    }

    @PreDestroy     //在销毁对象之前去调用它
    public void distory() {
        System.out.println("销毁AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }
}
