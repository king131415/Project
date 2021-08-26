package org.example.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Controller.Anotation.LoginRequre;
import org.example.Model.Event;
import org.example.Model.ResposePostAndMesg;
import org.example.Model.User;
import org.example.event.EventProducer;
import org.example.service.FollowService;
import org.example.service.UserService;
import org.example.tools.ActivityEnty;
import org.example.tools.Page;
import org.example.tools.UserMsgStrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 关注与取关
 */
@Controller
public class FollowController implements ActivityEnty {

    private static Gson gson = new GsonBuilder().create();


    @Autowired
    private FollowService followService;

    @Autowired
   private UserMsgStrage userMsgStrage;

    @Autowired
   private UserService userService;

    @Autowired
    private EventProducer eventProducer;




    //关注
    @PostMapping("/follow")
    @LoginRequre
    @ResponseBody
    public String follow(int entityType,int entityId){
        User user=userMsgStrage.getuser();

        followService.follow(user.getId(),entityType,entityId);

        //触发关注事件
        Event event=new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(userMsgStrage.getuser().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);


        ResposePostAndMesg respon=new ResposePostAndMesg();
        respon.setCode(0);
        respon.setMsg("已关注！");
        String s = gson.toJson(respon);


        return s;

    }

    //取关
    @PostMapping("/unfollow")
    @LoginRequre
    @ResponseBody
    public String Unfollow(int entityType,int entityId){
        User user=userMsgStrage.getuser();

        followService.Unfollow(user.getId(),entityType,entityId);
        ResposePostAndMesg respon=new ResposePostAndMesg();
        respon.setCode(0);
        respon.setMsg("已取消关注！");
        String s = gson.toJson(respon);

        return s;
    }

    //查询某个用户关注的人
    @GetMapping("/followees/{userId}")
    public String getFollowess(@PathVariable("userId") int userId, Page page, Model model){
        User user= userService.FindUserById(userId);
        if(user==null){
            throw new RuntimeException("该用户不存在！");

        }
        model.addAttribute("user",user);

        page.setPath("/followees/"+userId);
        page.setLimit(5);
        page.setRows((int)followService.findFolloweeCount(userId,ENTITY_TYPE));

        List<Map<String,Object>> userList=followService.findFollowees(userId,page.getStart(),page.getLimit());
        if(userList!=null){
            for(Map<String,Object> map:userList){
                User u=(User) map.get("user");
                map.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);
        return "/site/followee";
    }

    //查询某个用户的粉丝
    @GetMapping("/followers/{userId}")
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model){
        User user= userService.FindUserById(userId);
        if(user==null){
            throw new RuntimeException("该用户不存在！");

        }
        model.addAttribute("user",user);

        page.setPath("/followers/"+userId);
        page.setLimit(5);
        page.setRows((int)followService.findFollowerCount(ENTITY_TYPE,userId));

        List<Map<String,Object>> userList=followService.findFollowers(userId,page.getStart(),page.getLimit());
        if(userList!=null){
            for(Map<String,Object> map:userList){
                User u=(User) map.get("user");
                map.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);
        return "/site/follower";
    }


    //判断当前用户是否关注了目标用户
    private boolean hasFollowed(int userId){
        if(userMsgStrage.getuser()==null){
            return false;
        }

        return followService.hasFollowed(userMsgStrage.getuser().getId(),ENTITY_TYPE,userId);
    }
}
