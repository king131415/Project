package org.example.Config;


import org.example.Controller.Intercepter.LoginInterceptor;
import org.example.Controller.Intercepter.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//拦截器的配置类
@Configuration
public class LoginInterceptorConfig implements WebMvcConfigurer {

   @Autowired
   private    LoginInterceptor loginInterceptor;
   @Autowired
   private MessageInterceptor messageInterceptor;

    //添加拦截器对象,对那些路径拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //除了登录，注册，验证码，获取头像，静态资源，其他的都进行拦截
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*jpeg","/login","/register",
                                     "/kaptcha","/user/header/*");
        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*jpeg");


    }
}
