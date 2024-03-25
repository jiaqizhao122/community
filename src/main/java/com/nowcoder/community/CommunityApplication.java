package com.nowcoder.community;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication   //标识配置类，但是通常是程序的入口用这个
public class CommunityApplication {

    @PostConstruct   //用于管理bean的生命周期，初始化的方法，该注解所修饰的方法，会在构造器调用完以后执行
    public void init() {
        // 解决netty启动冲突问题
        // see Netty4Utils.setAvailableProcessors()
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {

        SpringApplication.run(CommunityApplication.class, args);
    }

}
