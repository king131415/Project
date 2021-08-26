package org.example.service;


import org.example.Mapper.DiscussPostMapper;
import org.example.Model.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowDiscussPostDetailService {

    @Autowired
   private   DiscussPostMapper discussPostMapper;

    public DiscussPost findDiscussPostByid(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }
}
