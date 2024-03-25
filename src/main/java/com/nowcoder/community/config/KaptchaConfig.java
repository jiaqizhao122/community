package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration  //普通配置类标识，表示这个类是一个配置类，不是一个普通的类
public class KaptchaConfig {
    @Bean
    public Producer kaptchaProducer(){        //通过properties往config里传参数，通过config往kaptcha里传参数，然后返回kaptcha,通过容器可以得到一个Producer实例
        Properties properties=new Properties();
        properties.setProperty("kaptcha.image.width", "100");
        properties.setProperty("kaptcha.image.height", "40");
        properties.setProperty("kaptcha.textproducer.font.size", "32");
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYAZ");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");

        DefaultKaptcha kaptcha=new DefaultKaptcha();
        Config config=new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

    //就是实例化Producer接口
    //将这个方法的返回的对象将被装配到容器里，bean的名字为方法名
}
