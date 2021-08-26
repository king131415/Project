package org.example.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Controller.Anotation.LoginRequre;
import org.example.Model.Event;
import org.example.Model.User;
import org.example.event.EventProducer;
import org.example.service.LikeService;
import org.example.tools.ActivityEnty;
import org.example.tools.UserMsgStrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class likeController implements ActivityEnty {

    private static Gson gson = new GsonBuilder().create();


    @Autowired
    private LikeService likeService;
    @Autowired
    private UserMsgStrage userMsgStrage;

    @Autowired
    private EventProducer eventProducer;



    //点赞
//    @LoginRequre
    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType, int entityId,int entityUserId,int postId) {
        User user = userMsgStrage.getuser();
        Map<String, Object> map = new HashMap<>();
        if(user==null){ //判断是否登录
            map.put("code",1);
            map.put("msg","您没有登录不能点赞！");
            String resp = gson.toJson(map);
            return resp;
        }

        //实现点赞
        likeService.like(user.getId(), entityType, entityId,entityUserId);
        //数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        //状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        //返回的结果用map装
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);
        map.put("code",0);
        map.put("msg",null);
        String s = gson.toJson(map);

        //触发点赞事件
        if(likeStatus==1){ //判断点赞的状态
            Event event=new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(userMsgStrage.getuser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId",postId);
            eventProducer.fireEvent(event);

        }


        return s;
    }
}
