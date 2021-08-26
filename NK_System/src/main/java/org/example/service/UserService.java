package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.example.Mapper.UserMapper;
import org.example.Model.LoginTicket;
import org.example.Model.User;
import org.example.tools.ActivityEnty;
import org.example.tools.MailSend;
import org.example.tools.NK_Util;
import org.example.tools.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements ActivityEnty {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //注入发邮件的工具类
    @Autowired
    private MailSend mailSend;

    //注入模板引擎对象
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${NK_System.path.domain}")
    private String damain;  //域名

    @Value("${server.servlet.context-path}")
    private  String contextpath; //应用上下文路径


    //根据用户Id去查询用户
    public User FindUserById(int userId){
//        return userMapper.selectById(userId);
         User user=getCache(userId); //先从缓存中取
         if(user==null){
          user= initCache(userId);//取不到，从数据库中取
        }
        return user;
    }

    //注册服务
    public Map<String,Object> register(User user){
        Map<String,Object> map=new HashMap<>();
        //空值处理
        if(user==null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMassge","账号不能为空");
            return map;
        }

        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMassge","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMassge","邮箱不能为空");
            return map;
        }

        //验证账号
        User u = userMapper.selectByName(user.getUsername());
        if(u!=null){
            map.put("usernameMassge","账号已存在");
            return map;
        }
        //验证邮箱
         u = userMapper.selctByEmail(user.getEmail());
        if(u!=null){
            map.put("emailMassge","邮箱已存在");
            return map;
        }
        //注册用户
        user.setSalt(NK_Util.getUUiD().substring(0,5));
        //密码加盐加密后存入,以后的登录通过提交的密码+存在数据库中的盐通过MD5加密后和存在数据库中的密码比较
        user.setPassword(NK_Util.getPasswordMd5(user.getPassword()+user.getSalt()));

        user.setType(0);
        user.setStatus(0);
        //设置激活码
        user.setActivationCode(NK_Util.getUUiD());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        //把用户信息插入数据库
        userMapper.insertUser(user);

        //注册成功后，发送激活邮件
        Context context=new Context();
        //给模板页面的变量设置参数
        context.setVariable("email",user.getEmail());

        String url=damain+contextpath+"/activation/"+user.getId() +"/" +user.getActivationCode();
        //给模板页面的变量设置参数
        context.setVariable("url",url);
       //利用模板引擎将HTML页面生成字符串
        String content=templateEngine.process("/mail/activation",context);

        mailSend.sendMail(user.getEmail(),"激活账号",content);
        return map;

    }
    //激活服务
    public int activetion(int userId,String code){
        User user=userMapper.selectById(userId);
        if(user.getStatus()==1){ //去数据库中查询,查到了状态等于1说明已经激活,因为注册时状态为0
            return ACTIVATION_REPEAT;  //表示重复激活
        }else if(user.getActivationCode().equals(code)){ //激活操作!!!
            //根据激活邮件第一次连接到此处,就激活成功,并把激活码设置为1
            userMapper.updateStatus(userId,1);
            clearCache(userId); //清除缓存
            return ACTIVATION_SUCCESS;
        }else {
            //其他特殊情况,激活失败
            return ACTIVATION_FAIL; //激活失败
        }

    }

    //用户登录服务
    public Map<String,Object> login(String username,String password,int expiredSecondd) throws ParseException {
        Map<String ,Object> map=new HashMap<>();

        //空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        //合法性
        User user=userMapper.selectByName(username);
        if(user==null){
            map.put("usernameMsg","该账号不存在！");
            return map;
        }
        if(user.getStatus()==0){
            map.put("usernameMsg","该账号未激活");
            return map;
        }
        //验证密码
        password= NK_Util.getPasswordMd5(password+user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","密码错误！");
            return map;
        }
//        //之前登录过然后注销了，继续登录就不给他发凭证了
//        int userId=user.getId();
//        LoginTicket loginTempticket = loginTicketMapper.selecrByLogginTicket(userId);
//        SimpleDateFormat df=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//        if(loginTempticket!=null){
//
//            Date parse = df.parse(loginTempticket.getExpired().toString());
//            long temp=parse.getTime()-System.currentTimeMillis();
//            if(temp>0){
//                loginTicketMapper.updateLoginTicket(loginTempticket.getTicket(),0);
//            }
//        }
        //生成登录凭证
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(NK_Util.getUUiD());
        loginTicket.setStatus(0); //有效
        loginTicket.setExpired(new Date(System.currentTimeMillis() +expiredSecondd*1000));

//     loginTicketMapper.insertLoginTicket(loginTicket); //存数据库不推荐了

        //登录凭证存在Redis里面,不存数据库了**********
        String redisKey= RedisKeyUtil.getLoginTicket(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey,loginTicket); //redis会把loginTicket序列化为JSON字符串
        //登录成功,将登录凭证装进Map放回给前端
        map.put("ticket",loginTicket.getTicket());
        return map;


    }

    //用户注销
    public void UserQuit(String ticket){
//        loginTicketMapper.updateLoginTicket(ticket,1);
        String redisKey= RedisKeyUtil.getLoginTicket(ticket);
        LoginTicket  loginTicket =(LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey,loginTicket);


    }

    //根据凭证查询凭证表中的所有信息
    public LoginTicket findLoginTicket(String ticket){

//     return loginTicketMapper.selectByLoginTicket(ticket);
        String redisKey=RedisKeyUtil.getLoginTicket(ticket);
        return (LoginTicket)redisTemplate.opsForValue().get(redisKey);

    }

    //更新用户头像
    public int updateUserImge(int userId,String headerUrl){
        int rows= userMapper.updateHeader(userId,headerUrl);
        clearCache(userId);//清理缓存
        return rows;

    }
    //更换密码
    public int updatePassword(int userId,String password){
        return userMapper.updatePassword(userId,password);
    }

    //根据用户名查找用户
    public User findUserByName(String username){

        return userMapper.selectByName(username);
    }

    //1.优先从缓存中取值
    protected User getCache(int userId){
       String redisKey= RedisKeyUtil.getUserKey(userId);
       //尝试从redis里面取数据
       return  (User) redisTemplate.opsForValue().get(redisKey);
    }
    //2.取不到时，初始化缓存数据(往缓存里面存数据)
    protected User initCache(int userId){
        User user=userMapper.selectById(userId);
        String redisKey= RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey,user,3600,TimeUnit.SECONDS);//过期时间
        return user;
    }
    //3.数据变更时清除缓存数据
    protected void clearCache(int userId){
        String redisKey=RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

}
