package org.example.Controller;

import org.apache.commons.lang3.StringUtils;
import org.example.Controller.Anotation.LoginRequre;
import org.example.Model.User;
import org.example.service.UserService;
import org.example.tools.ActivityEnty;
import org.example.tools.NK_Util;
import org.example.tools.UserMsgStrage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements ActivityEnty {

    //准备日志记录对象
    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Value("${NK_System.path.upload}")
    private String updatePath; //存放路径

    @Value("${NK_System.path.domain}")
    private String domain;  //域名

    @Value("${server.servlet.context-path}")
    private String contextPath; //应用上下文路径

    @Autowired
    private UserService userService;

    @Autowired
    UserMsgStrage userMsgStrage; //当前用户，要从当前用户请求线程中存储的去取


    //上传头像
    @LoginRequre
    @PostMapping("/update")
    public String UpdateImge(MultipartFile headerImge, Model model){
        if(headerImge==null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }
        //获取上传的文件名
        String filename = headerImge.getOriginalFilename();
        String endFile = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(endFile)){ //检查长传的格式
            model.addAttribute("error","文件格式不正确！");
            return "/site/setting";
        }
        //生成随机的文件名
        filename= NK_Util.getUUiD()+endFile;
        //文件的存放的路径
        File newFile=new File(updatePath+"/"+filename);
//       if(!newFile.exists()){
//           newFile.mkdirs(); //创建文件
//       }
        try {
            headerImge.transferTo(newFile); //将用户上传的文件照片写入到相应创建的文件里
        } catch (IOException e) {
            logger.error("上传文件失败:"+e.getMessage());
            throw new RuntimeException("文件上传失败,服务异常！！");
        }
        //更新照片的访问路径
        User user=userMsgStrage.getuser(); //获取当前用户
        String headerUrl=domain+contextPath+"/user/header/"+filename;
        //更行用户的头像的路径
        userService.updateUserImge(user.getId(),headerUrl);

        return "redirect:/index";
    }

    //跳转到新的页面后的图片标签的请求==>从HeaderURl中截取出来的
    @GetMapping("/header/{filename}")
    public void getHeaderImge(@PathVariable("filename") String filename, HttpServletResponse response){ //是向浏览器返回二进制数据，需要手动调用Response对象
        //服务器存放的路径
        filename=updatePath+"/"+filename;
        String last = filename.substring(filename.lastIndexOf("."));

        //相应类型==>返回类型
        response.setContentType("image/"+last);
        try(
                OutputStream os = response.getOutputStream();
                FileInputStream in = new FileInputStream(filename);
        ) {

            byte[] data = new byte[1024];
            int len;
            while ((len = in.read(data)) != -1) {
                os.write(data, 0, len);
            }
        }catch (IOException e){
            logger.error("读取头像失败"+e.getMessage());
        }
    }

    //修改密码服务
    @LoginRequre
    @GetMapping("/UpdatePass")
    public String updatePassword(String oldPassword, String newPassword,
                                 String confirmPassword, Model model){
        //检查合法性
        if(StringUtils.isBlank(oldPassword)){
            model.addAttribute("passwordError","原密码不能为空!");
            return "/site/setting";
        }
        //验证密码是否输入正确
        User user = userMsgStrage.getuser();
        if(!user.getPassword().equals(oldPassword)){
            model.addAttribute("passwordError","原密码错误!!");
            return "/site/setting";
        }
        //判断两次密码是否一致
        if(!newPassword.equals(confirmPassword)){
            model.addAttribute("passwordError1","两次输入密码不一致!!!");
            return "/site/setting";
        }
        //更换密码
        int userId=user.getId();
        int n = userService.updatePassword(userId, newPassword);
        return "redirect:/index";

    }

    //注销
    @GetMapping("/quit")
    public String UserQuit(@CookieValue("ticket") String ticket){
        //用户退出的话，就修改登录凭证,把状态改为1
        userService.UserQuit(ticket);
        return "redirect:/login";
    }

    //个人设置页面
    @LoginRequre
    @GetMapping("/setting")
    public String getSettingPage(){
        return "/site/setting";
    }


}
