package org.example.event;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.example.Model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    private static Gson gson= new  GsonBuilder().create();

    @Autowired   //注入Kafka发送事件的类
    private KafkaTemplate kafkaTemplate;

    //处理事件
    public void fireEvent(Event event){
        //将事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(),gson.toJson(event));
    }
}
