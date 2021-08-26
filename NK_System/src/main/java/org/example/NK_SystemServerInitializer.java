package org.example;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * 由于Tomcat也是Java编写的，自生就靠Main启动的，一个程序不肯有对各Main
 * 所以需要配置，通过继承SpringBootServletInitializer这个类，Tomcat会自动扫描
 * 这个类，然后通过运行configure方法启动SpringBoot项目
 */
public class NK_SystemServerInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(NkApplication.class);
    }
}
