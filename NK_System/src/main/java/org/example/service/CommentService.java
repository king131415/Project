package org.example.service;

import org.example.Mapper.CommentMapper;
import org.example.Model.Comment;
import org.example.tools.ActivityEnty;
import org.example.tools.SesitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService implements ActivityEnty {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SesitiveFilter sesitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    //查询帖子或者评论的评论
   public List<Comment> findConmmentByEntity(int entityType,int entityId,int start,int limit){
       return commentMapper.selectCommentByEntity(entityType,entityId,start,limit);
   }
   //查询帖子或者评论的评论总数
   public int findCommentCount(int entityType,int entityId){
       return commentMapper.selectCountByEntity(entityType,entityId);
   }

    //增加评论的业务
    //定义事务的隔离级别，以及传播方式
   @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
   public  int addComment(Comment comment){
       if(comment==null){
           throw new IllegalArgumentException("参数不能为空！");

       }
       //添加评论
       comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
       comment.setContent(sesitiveFilter.filterString(comment.getContent()));

       int rows=commentMapper.insertComment(comment);

       //更新帖子的评论数量
       if(comment.getEntityType()==ENTITY_TYPE_POST){
           int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
           discussPostService.updateCommentCount(comment.getEntityId(),count);
       }

       return rows;

   }
   public Comment findCommentById(int id){
       return commentMapper.selectCommentById(id);
   }

}
