package org.example.Controller;

import org.example.Controller.Anotation.LoginRequre;
import org.example.Model.Comment;
import org.example.Model.DiscussPost;
import org.example.Model.Event;
import org.example.event.EventProducer;
import org.example.service.CommentService;
import org.example.service.DiscussPostService;
import org.example.tools.ActivityEnty;
import org.example.tools.UserMsgStrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements ActivityEnty {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserMsgStrage userMsgStrage;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private EventProducer eventProducer;

    //添加评论，要更新帖子的评论数量
    @LoginRequre
    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId") int discussPostId,
                             Comment comment){

        comment.setUserId(userMsgStrage.getuser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //触发评论事件
        Event event=new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(userMsgStrage.getuser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId",discussPostId);
        if(comment.getEntityType()==ENTITY_TYPE_POST){ //如果评论的类型是帖子
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());

        }else if(comment.getEntityType()==ENTITY_TYPE_COMMENT){ //如果评论的类型是回复
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());

        }
        //发送通知
        eventProducer.fireEvent(event);


        //触发发帖事件，发送通知
        if(comment.getEntityType()==ENTITY_TYPE_POST){
           event=new Event()
                    .setTopic(TOPIC_SENDPOST)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);
        }


        return "redirect:/detail/" +discussPostId;
    }

}
