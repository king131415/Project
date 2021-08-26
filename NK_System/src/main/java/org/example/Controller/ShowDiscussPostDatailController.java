package org.example.Controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Model.*;
import org.example.event.EventProducer;
import org.example.service.*;
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

import java.util.*;

@Controller
public class ShowDiscussPostDatailController implements ActivityEnty {

    @Autowired
    private static Gson gson=new GsonBuilder().create();

    @Autowired
    private   ShowDiscussPostDetailService showDiscussPostDetailService;
    @Autowired
    private UserService userService;


    @Autowired
    private EventProducer eventProducer;


    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserMsgStrage userMsgStrage;


    //显示帖子详情
    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPostDetail(@PathVariable("discussPostId") int id, Model model, Page page){

        DiscussPost post=showDiscussPostDetailService.findDiscussPostByid(id);

        model.addAttribute("post",post);
        //根据帖子的相关的信息查询作者信息
        User user = userService.FindUserById(post.getUserId());
        //点赞有关的数量
        long likeCount=likeService.findEntityLikeCount(ENTITY_TYPE_POST,id);
        model.addAttribute("likeCount",likeCount);

        //点赞状态,若没有登录点赞状态就是0
        int likeStatus=userMsgStrage.getuser()==null? 0:
                likeService.findEntityLikeStatus(userMsgStrage.getuser().getId(),ENTITY_TYPE_POST,id);
        model.addAttribute("likeStatus",likeStatus);



        model.addAttribute("user",user);

        //查询评论的分页信息
        page.setLimit(5);
        page.setPath("/detail/"+ id);
        page.setRows(post.getCommentCount());
        //帖子的类型是1
        List<Comment> conmmentList = commentService.findConmmentByEntity(ENTITY_TYPE_POST, post.getId(), page.getStart(), page.getLimit());
        List<Map<String,Object>> commentVOlist=new ArrayList<>();
        if(conmmentList!=null){
            for(Comment comment:conmmentList){
                Map<String,Object> commentVo=new HashMap<>();
                //每次遍历把评论装进Map,也要把对应的评论对象装进Map
                commentVo.put("comment",comment);
                commentVo.put("user", userService.FindUserById(comment.getUserId()));


                likeCount=likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeCount",likeCount);
                //点赞状态,若没有登录点赞状态就是0
                 likeStatus=userMsgStrage.getuser()==null? 0:
                        likeService.findEntityLikeStatus(userMsgStrage.getuser().getId(),ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeStatus",likeStatus);


                //查询评论的评论==》回复
                List<Comment> replyList = commentService.findConmmentByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);

                List<Map<String,Object>> repleyVolist=new ArrayList<>();

                if(replyList!=null){
                    for(Comment reply:replyList){
                        Map<String,Object> replyVo=new HashMap<>();
                        //回复装进Map
                        replyVo.put("reply",reply);
                        //回复的作者装进Map
                        replyVo.put("user", userService.FindUserById(reply.getUserId()));

                        //回复目标
                       User target= reply.getTargetId()==0?null: userService.FindUserById(reply.getTargetId());
                       replyVo.put("target",target);

                       //点赞相关的信息
                        likeCount=likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeCount",likeCount);
                        //点赞状态,若没有登录点赞状态就是0
                        likeStatus=userMsgStrage.getuser()==null? 0:
                                likeService.findEntityLikeStatus(userMsgStrage.getuser().getId(),ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeStatus",likeStatus);


                        repleyVolist.add(replyVo);

                    }
                }
                commentVo.put("replys",repleyVolist);
                //回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount",replyCount);
                commentVOlist.add(commentVo);
            }
        }
        model.addAttribute("comments",commentVOlist);
        model.addAttribute("page",page);
        return "/site/discuss-detail";
    }

    //置顶
    @PostMapping("/top")
    @ResponseBody
    public String setPostType_Top(int id){

        User user= userMsgStrage.getuser();
        if(user==null){
            return null;
        }

        discussPostService.updateDiscussPostType(id,1);
        //同步到ES
        //触发发帖事件
        Event event=new Event()
                .setTopic(TOPIC_SENDPOST)
                .setUserId(userMsgStrage.getuser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        //返回响应数据
        ResposePostAndMesg msg=new ResposePostAndMesg();
        msg.setCode(0);
        msg.setMsg(null);
        return gson.toJson(msg);
    }

    //加精
    @PostMapping("/wonderful")
    @ResponseBody
    public String setPostStatus_wonderful(int id){


        User user= userMsgStrage.getuser();
        if(user==null){
            return null;
        }

        DiscussPost post= discussPostService.findDiscussPostById(id);
        int count1=post.getCommentCount(); //评论数量
        int count2=(int)likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());//点赞数

        double score=count1+count2;
        discussPostService.updateDiscussPostStatus(id,1);
        //设置分数
        discussPostService.updateDiscussPostScore(id,score);

        //同步到ES
        //触发发帖事件
        Event event=new Event()
                .setTopic(TOPIC_SENDPOST)
                .setUserId(userMsgStrage.getuser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        //返回响应数据,成功发布
        ResposePostAndMesg msg=new ResposePostAndMesg();
        msg.setCode(0);
        msg.setMsg(null);
        return gson.toJson(msg);
    }

    //删除
    @PostMapping("/delete")
    @ResponseBody
    public String setPostStatus_delete(int id){


        User user= userMsgStrage.getuser();
        if(user==null){
            return null;
        }

        discussPostService.updateDiscussPostStatus(id,2);

         //同步到ES
        //触发删帖事件
        Event event=new Event()
                .setTopic(TOPIC_DELETE)
                .setUserId(userMsgStrage.getuser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        //返回响应数据,成功发布
        ResposePostAndMesg msg=new ResposePostAndMesg();
        msg.setCode(0);
        msg.setMsg(null);
        return gson.toJson(msg);
    }

}
