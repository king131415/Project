package org.example.service;

import org.example.Mapper.DiscussPostMapper;
import org.example.Model.DiscussPost;
import org.example.tools.SesitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SesitiveFilter sesitiveFilter;

    //查询所有的帖子
    public List<DiscussPost> fingDiscussPost(int userId,int Start,int limit){
        return  discussPostMapper.selectDiscussPosts(userId,Start,limit);
    }
     //根据用户Id查询某个帖子
     public int findDiscussCount(int userId){
        return discussPostMapper.selectDiscussPostCout(userId);
    }

    //发布帖子的服务
    public int addDiscussPost(DiscussPost post){
        if(post==null){
            throw  new IllegalArgumentException("参数不能为空！");
        }
        //利用框架去掉特殊的标签
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
       //过滤敏感词
        post.setTitle(sesitiveFilter.filterString(post.getTitle()));
        post.setContent(sesitiveFilter.filterString(post.getContent()));
        return discussPostMapper.insertDiscussPost(post);

    }
    //更新帖子的评论数量
    public int updateCommentCount(int id,int commentCount){
        return discussPostMapper.updateCommentCount(id,commentCount);
    }
    
    //根据Id查询帖子
    public DiscussPost findDiscussPostById(int id ){
        return discussPostMapper.selectDiscussPostById(id);
    }


    //修改帖子的类型
    public int updateDiscussPostType(int id,int type){
        return discussPostMapper.updateDiscusspostType(id,type);
    }

    //修改帖子的状态
    public int updateDiscussPostStatus(int id,int status){
        return discussPostMapper.updateDiscussStatus(id,status);
    }

    //修改帖子的分数
    public int updateDiscussPostScore(int id,double score){
        return discussPostMapper.updateDiscssPostScore(id,score);
    }
}
