package org.example.Controller.EsController;

import org.example.Model.DiscussPost;
import org.example.service.ES.EsService;
import org.example.service.LikeService;
import org.example.service.UserService;
import org.example.tools.ActivityEnty;
import org.example.tools.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements ActivityEnty {

    @Autowired
    private EsService esService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping("/search")
    public  String search(String keyword, Page page, Model model){
        //搜索帖子
        Map<String, Object> Postmap = esService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());

        List<DiscussPost> searchResult =(List<DiscussPost>)Postmap.get("posts");
        //处理结果集,返回界面需要用到的数据
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        if(!searchResult.isEmpty()){
            for(DiscussPost post:searchResult){
                Map<String,Object> map=new HashMap<>();
                //帖子
                map.put("post",post);
                //作者
                map.put("user",userService.FindUserById(post.getUserId()));
                //点赞的数量
                map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId()));
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("keyword",keyword);

        //分页相关的信息
        page.setPath("/search?keyword="+keyword);
        //查询该词下所有的数据
        page.setRows(searchResult==null?0:(int)Postmap.get("count"));
        return "/site/search";
    }


}
