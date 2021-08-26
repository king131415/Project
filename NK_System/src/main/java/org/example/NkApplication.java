package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class NkApplication {
    /**
     *  @PostConstruct ==>可以用于管理Bean的生命周期
     *  由他修饰的方法，会在构造器调用完后，就开始调用
     */
    //此处解决Netty启动冲突问题
    @PostConstruct
    public void init(){
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
    public static void main(String[] args) {
        //会返回SpringIOC容器
//        ConfigurableApplicationContext run = SpringApplication.run(NkApplication.class, args);
        SpringApplication.run(NkApplication.class, args);
    }
}
