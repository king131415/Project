package org.example.Controller;

import org.example.Model.User;
import org.example.service.FollowService;
import org.example.service.LikeService;
import org.example.service.UserService;
import org.example.tools.ActivityEnty;
import org.example.tools.UserMsgStrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *个人主页
 */
@Controller
public class ShowProFileController implements ActivityEnty {
    @Autowired
     private UserService userService;
    @Autowired
     private LikeService likeService;
    @Autowired
    private FollowService followService;
    @Autowired
    private UserMsgStrage userMsgStrage;

    //个人主页
    @GetMapping("/profile/{userId}")
    public String getUserFile(@PathVariable("userId") int userId, Model model){
        User user= userService.FindUserById(userId);
        if(user==null){
            throw  new RuntimeException("用户不存在！");
        }
        //用户
        model.addAttribute("user",user);
        //点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);
        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE);
         model.addAttribute("followeeCount",followeeCount);

        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE, userId);
        model.addAttribute("followerCount",followerCount);

        //是否关注某个实体
        boolean hasFollowed=false;
        //先判断是否登录
        if (userMsgStrage.getuser()!=null){
            hasFollowed=followService.hasFollowed(userMsgStrage.getuser().getId(),ENTITY_TYPE,userId);
        }
        model.addAttribute("hasFollowed",hasFollowed);

        return "/site/profile";
        
    }
}
