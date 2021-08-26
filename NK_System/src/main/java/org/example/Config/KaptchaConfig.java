package org.example.Config;


import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean
   public Producer kaptcherProducer(){
        //DefaultKaptcha是Producer的实现类，是用来产生随机验证码图片的
        DefaultKaptcha kaptcha=new DefaultKaptcha();
        Properties properties=new Properties();
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.image.height","40");
        properties.setProperty("kaptcha.textproducer.font.size","32");
        properties.setProperty("kaptcha.textproducer.font.color","0,0,0");
        properties.setProperty("kaptcha.textproducer.char.string","0123456789ABCDEFGHIJKLNMOPQRSTUVWXYZ邱琦半边天王总");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise"); //防止暴力破解 ==>在图片加点阴影

        Config config=new Config(properties); //创建配置类
        kaptcha.setConfig(config); //添加配置
        return kaptcha;

    }

}
