package org.example.Controller;


import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.example.Model.User;
import org.example.service.UserService;
import org.example.tools.ActivityEnty;
import org.example.tools.NK_Util;
import org.example.tools.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginAndRegisterController implements ActivityEnty {


    //定义日志记录对象
    private static final Logger logger= LoggerFactory.getLogger(LoginAndRegisterController.class);


    //验证码对象注入(没有指定的话，就是以方法名命名的Bean象名
    @Autowired
    private Producer kaptcharProducer;


    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Value("${server.servlet.context-path}")
    private String contextPath;


    //点击登录就跳转到登录页面
    @GetMapping("/login")
    public String getLoginPage(Model model){
        return "/site/login";
    }

    //点击注册就跳转到注册页面
    @GetMapping("/register")
    public  String getRegisterPage(){
        return "/site/register";
    }


    //激活操,用户点击激活连接,激活账号 ==>激活服务
    @GetMapping("/activation/{userId}/{code}")  //@PathVariable ==>是获取URI中的参数,如果是jSON的话就要使用@ResqBody 这样才能准确获取
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        int result=userService.activetion(userId,code);
        if(result==ACTIVATION_SUCCESS){ //激活成功
            model.addAttribute("msg","激活成功!您的账号可以正常使用了");
            model.addAttribute("target","/login");

        }else if(result==ACTIVATION_REPEAT){
            model.addAttribute("msg","操作无效,该账号已经激活!");
            model.addAttribute("target","/index");

        }else {
            model.addAttribute("msg","激活失败,您提供的激活码错误!");
            model.addAttribute("target","/index");

        }
        return "/site/operate-result";

    }
    //登录服务
    @PostMapping("/login")
    public String  login(String username, String password, String code, boolean remeberme, Model model ,
                         /*HttpSession session,*/ HttpServletResponse response, @CookieValue("KaptchaOwner") String KaptchaOwner ) throws ParseException {
        //        //获取验证码
        //        String kaptcha= (String) session.getAttribute("kaptcha");

        String kaptcha=null;
        if(StringUtils.isNotBlank(KaptchaOwner)){
            String redisKey=RedisKeyUtil.getKaptchKey(KaptchaOwner);
            kaptcha=(String)redisTemplate.opsForValue().get(redisKey);

        }
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equals(code)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";  //验证码不正确，返回登录页
        }
        //检查账号密码
        int expressSecond=remeberme?REMEBERME_EXTRA_SECOND:DEFUALT_EXTRAT_SECOND;
        Map<String, Object> map = userService.login(username, password, expressSecond);
        if(map.containsKey("ticket")){
            Cookie cookie=new Cookie("ticket",map.get("ticket").toString()); //设置Cookie
            cookie.setPath(contextPath);
            //cookie的有效时间
            cookie.setMaxAge(expressSecond);
            response.addCookie(cookie);

            return "redirect:/index";
        }else {
            //添加错误响应给视图，返回给前端
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));

            return "/site/login";
        }
    }

    //用户注册服务
    @PostMapping("/register")
    public  String register(Model model, User user){  //此处的User 就是 Pojo类
        //此时的User已经装进Model里面了
        //根据前台传过来的数据,SpringMVC会基于数据的类型赋值给相应的实体类
        Map<String, Object> map =userService.register(user);
        if(map==null || map.isEmpty()){ //注册成功跳转首页(因为还没有激活),激活后跳转登录页
            model.addAttribute("msg","注册成功!已经向您的邮箱发送了激活邮件!请尽快激活!");
            model.addAttribute("target","/index");
            return "/site/operate-result";   //中间结果层页面
        }else {
            //usernameMassge  passwordMassge  emailMassge
            //添加到视图层
            model.addAttribute("usernameMassge",map.get("usernameMassge"));
            model.addAttribute("passwordMassge",map.get("passwordMassge"));
            model.addAttribute("emailMassge",map.get("emailMassge"));

            return "/site/register";  //注册失败,返回原页面,显示错误信息
        }

    }
    //向前台返回验证码图片,验证码服务
    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response/*, HttpSession session*/)  {
        //生成验证码
        //1.生成随机字符串
        String text = kaptcharProducer.createText();
        //2.根据随机字符串，生成随机验证码图片
        BufferedImage image = kaptcharProducer.createImage(text);
                //        //验证码存到Session
                //        session.setAttribute("kaptcha",text);
        //验证码的归属
        String KaptchaOwner= NK_Util.getUUiD();
        Cookie cookie=new Cookie("KaptchaOwner",KaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie); //返回给浏览器

        //将验证码存入redis
        String redisKey= RedisKeyUtil.getKaptchKey(KaptchaOwner);
        //超过60S就是失效了
        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);

        //将验证码图片输出给浏览器 ==>设置响应格式
        response.setContentType("image/png");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream(); //字节流
            ImageIO.write(image,"png",outputStream);
//            response.getWriter().write(image.toString());
        } catch (IOException e) {
            //出现异常日志记录一下
            logger.error("响应验证失败:"+e.getMessage());
        }
    }


}
