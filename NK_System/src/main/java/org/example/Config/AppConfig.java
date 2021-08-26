package org.example.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * 装配第三方的Bean
 *
 * @Configuration ==>标识当前类为配置类，
 * 扫描时才能将其类下的@bean修饰的方法装配到容器中去
 */

@Configuration
public class AppConfig {

    /**
     *  @Bean 注解的方法，返回值的对象会被装配到容器中去
     *
     */

    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
