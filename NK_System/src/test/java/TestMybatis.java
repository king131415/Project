
import org.example.Mapper.DiscussPostMapper;
import org.example.Mapper.LoginTicketMapper;
import org.example.Mapper.MessageMapper;
import org.example.Mapper.UserMapper;
import org.example.Model.DiscussPost;
import org.example.Model.LoginTicket;
import org.example.Model.User;
import org.example.NkApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NkApplication.class)
public class TestMybatis {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    DiscussPostMapper discussPostMapper;
    @Autowired
    LoginTicketMapper loginTicketMapper;
    @Autowired
    MessageMapper messageMapper;


    /**
     * SpringBoot整合Mybatis的运行原理：
     *   1:首先SpringBoot启动时候，会基于SPI实现自动装配
     *   2:在Spring.factories下的MybatiesAutoConfiguration这个配置类，通过这个配置类扫描指定的.xml（我们自己写的.xml配置文件）来实例化sqlSeesionFactory和sqlSession对象
     *   3:在SpringBoot启动刷新应用上下文的时候，会执行SpringApplication的refreshContext方法，在这个方法中会扫描含有@mapper注解的类，然后基于JDK动态代理的方式生成代理类
     *   5：执行代理对象的invike方法（实际上就是执行对应的对应SqlSession)
     *
     */





    @Test
    public  void  testSelectUser(){
        User user=userMapper.selectById(101);
        System.out.println(user);

        user=userMapper.selectByName("liubei");
        System.out.println(user);
        user=userMapper.selctByEmail("1292203381@qq.com");
        System.out.println(user);
    }

    @Test
    public void TestInsert(){
        User user=new User();
        user.setUsername("邱琦");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());

    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "hello");
        System.out.println(rows);
    }


    @Test
    public void SelectDiscssPost(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10);

        for(DiscussPost discussPost:discussPosts){
            System.out.println(discussPost);
        }

        int row =discussPostMapper.selectDiscussPostCout(0);
        System.out.println(row);


    }
    @Test
    public void testLoginTicket(){
//        LoginTicket loginTicket=new LoginTicket();
//        loginTicket.setUserId(101);
//        loginTicket.setExpired(new Date(System.currentTimeMillis()+ 1000*60*10));
//        loginTicket.setStatus(0);
//        loginTicket.setTicket("aaa");
//        int i = loginTicketMapper.insertLoginTicket(loginTicket);
//        System.out.println(i);

        LoginTicket loginTicket1 = loginTicketMapper.selectByLoginTicket("aaa");
        System.out.println(loginTicket1);

        int a = loginTicketMapper.updateLoginTicket("aaa", 1);
        System.out.println(a);


    }
    @Test
    public  void  testinsertDiscussPost(){
        DiscussPost discussPost=new DiscussPost();
        discussPost.setUserId(159);
        discussPost.setTitle("ccc");
        discussPost.setContent("XXXXXXX");
        discussPost.setType(0);
        discussPost.setStatus(0);
        discussPost.setCreateTime(new Date());
        discussPost.setCommentCount(2);
        discussPost.setScore(9);
        int i = discussPostMapper.insertDiscussPost(discussPost);
        System.out.println(i);
    }
    @Test
    public void testSelectLetters(){
//        List<Massage> massages = massageMapper.selectConversation(111, 0, 20);
//        for(Massage m:massages){
//            System.out.println(m);
//        }
//        int count=massageMapper.selectConvsersationCount(111);
//        System.out.println(count);

//        List<Massage> massages1 = massageMapper.selectLetter("111_112", 0, 10);
//        for(Massage m:massages1){
//            System.out.println(m);
//
//        }
        int a= messageMapper.selectLetterCount("111_112");
        System.out.println(a);

//        int unreader=massageMapper.selectLetterUnreader(131,"111_131");
//        System.out.println(unreader);

    }


}
