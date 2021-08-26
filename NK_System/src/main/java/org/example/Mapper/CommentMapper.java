package org.example.Mapper;


import org.apache.ibatis.annotations.Mapper;
import org.example.Model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {


    //查询某种类型的评论的所有评论
    List<Comment>  selectCommentByEntity(int entityType,int entityId,int start,int limit);


    //查询某种类型的评论总数
    int  selectCountByEntity(int entityType,int entityId);

    //添加一条评论
    int insertComment(Comment comment);

    Comment selectCommentById(int id);



}
