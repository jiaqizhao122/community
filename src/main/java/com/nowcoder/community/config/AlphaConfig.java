package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration  //普通配置类标识，表示这个类是一个配置类，不是一个普通的类
public class AlphaConfig {

    @Bean   //将这个方法的返回的对象将被装配到容器里，bean的名字为方法名
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

}
