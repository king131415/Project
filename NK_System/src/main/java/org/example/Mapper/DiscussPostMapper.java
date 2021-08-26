package org.example.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.Model.DiscussPost;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DiscussPostMapper {
     //此处时候是动态的SQL,未登录时就是查询所有的帖子
    //当用户登录时，就是查询用户发布的帖子
    List<DiscussPost> selectDiscussPosts(int userId,int start,int limit);

    //查询所有的贴，也是动态的SQL  @Param("userId") ==>类似取别名的，在动态的SQL能够防止无法识别
    int  selectDiscussPostCout(@Param("userId")int userId);

    //插入一条帖子
    int insertDiscussPost(DiscussPost discussPost);

    //根据id查询一条帖子
    DiscussPost selectDiscussPostById(int id);

    //更新帖子的评论数量
    int updateCommentCount(int id,int commentCount);

    //修改帖子的类型
    int updateDiscusspostType(int id,int type);


    //修改帖子的状态
    int updateDiscussStatus(int id,int status);

    //修改帖子的分数
    int updateDiscssPostScore(int id,double score);


}
