package org.example.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Controller.Anotation.LoginRequre;
import org.example.Model.DiscussPost;
import org.example.Model.Event;
import org.example.Model.ResposePostAndMesg;
import org.example.Model.User;
import org.example.event.EventProducer;
import org.example.service.DiscussPostService;
import org.example.tools.ActivityEnty;
import org.example.tools.UserMsgStrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class AddDicussController implements ActivityEnty {


    private static Gson gson=new GsonBuilder().create();

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private EventProducer eventProducer;


    @Autowired
    private   UserMsgStrage userMsgStrage;

    //发布帖子
    @LoginRequre
    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user=userMsgStrage.getuser();
        ResposePostAndMesg respose=new ResposePostAndMesg();
        if(user==null){
            respose.setCode(403);
            respose.setMsg("您还没有登录！");
            String result=gson.toJson(respose);
            return result;
        }

        DiscussPost post=new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        respose.setCode(0);
        respose.setMsg("发布成功！");

        String result=gson.toJson(respose);

        //触发发帖事件
        Event event=new Event()
                .setTopic(TOPIC_SENDPOST)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(post.getId());
        eventProducer.fireEvent(event);


        return result;
    }
}
