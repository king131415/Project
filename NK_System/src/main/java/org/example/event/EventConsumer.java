package org.example.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.Model.DiscussPost;
import org.example.service.ES.EsService;
import org.example.Model.Event;
import org.example.Model.Message;
import org.example.service.DiscussPostService;
import org.example.service.MessageService;
import org.example.tools.ActivityEnty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements ActivityEnty {
 
    private static Gson gson=new GsonBuilder().create();
    
    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private EsService esService;

    /**
     *私信表，不仅仅存了用户之间的私信，还存了，系统的通知
     */

    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_LIKE,TOPIC_FOLLOW})
    public void handleAllTopicMsg(ConsumerRecord record){
        if(record==null || record.value()==null){
            logger.error("消息内容为空！");
            return;
        }
        Event event=gson.fromJson(record.value().toString(),Event.class);
        if(event==null){
            logger.error("消息格式错误！");
            return;
        }

        //发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(gson.toJson(content));
        messageService.addMessage(message);
    }

    //消费发帖事件
    @KafkaListener(topics = {TOPIC_SENDPOST})
    public void handlePublishPost(ConsumerRecord record){
        if(record==null || record.value()==null){
            logger.error("消息内容为空！");
            return;
        }
        Event event=gson.fromJson(record.value().toString(),Event.class);
        if(event==null){
            logger.error("消息格式错误！");
            return;
        }

        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        esService.sevaDiscusspost(post); //存到ES服务器

    }

    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeletePost(ConsumerRecord record){
        if(record==null || record.value()==null){
            logger.error("消息内容为空！");
            return;
        }
        Event event=gson.fromJson(record.value().toString(),Event.class);
        if(event==null){
            logger.error("消息格式错误！");
            return;
        }
        esService.deleteDscuccPost(event.getEntityId());//存到ES服务器

    }

}
