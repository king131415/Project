package org.example.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    //参数上注入redis数据库连接对象
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){

        RedisTemplate<String,Object> template=new RedisTemplate<>();
        //设置连接对象
        template.setConnectionFactory(factory);

        //设置key的序列化方式
        template.setKeySerializer(RedisSerializer.string());

        //设置value的序列化方式
        template.setValueSerializer(RedisSerializer.json());

        //设置Hash的key的key的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());

        //设置Hash的value的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        return template;

    }
}
