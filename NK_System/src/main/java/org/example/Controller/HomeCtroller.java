package org.example.Controller;

import org.example.Model.DiscussPost;
import org.example.Model.User;
import org.example.service.DiscussPostService;
import org.example.service.LikeService;
import org.example.service.UserService;
import org.example.tools.ActivityEnty;
import org.example.tools.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeCtroller implements ActivityEnty {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userSevice;

    @Autowired
    private LikeService likeService;
    /**
     *返回String表示 视图的名字;
     * Model 携带数据给模板
     */
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){

        //设置讨论贴的总数
        page.setRows(discussPostService.findDiscussCount(0));
        page.setPath("/index");


        List<DiscussPost> list= discussPostService.fingDiscussPost(0, page.getStart(), page.getLimit());
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        System.out.println("===================");
        if(list!=null){
            System.out.println("----------------------" + list.size());
            for(DiscussPost post:list){
                Map<String,Object> map=new HashMap<>();
                map.put("post",post);//帖子装进集合
                User user=userSevice.FindUserById(post.getUserId());
                System.out.println("===========user: " + user);
                map.put("user",user); //把用户也装进集合，就是为了获得相应的用户名

                //查询帖子点赞数
                long likecCount=likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likecCount);
                discussPosts.add(map); //最后都装到父集合中去
            }
        }

        //其实这一步是可以不用写的，因为框架在我们调用page对象的方法时，把我们的对象实例化了，并且装进了Model层了
         model.addAttribute("page",page);
        //添加到视图层模板
         model.addAttribute("discussPosts",discussPosts);
         return "index";  //转化的地址类似，给视图层
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "/error/500";
    }

}
