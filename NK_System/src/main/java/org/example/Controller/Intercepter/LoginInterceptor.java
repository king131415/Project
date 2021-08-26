package org.example.Controller.Intercepter;


import org.example.Controller.Anotation.LoginRequre;
import org.example.Model.LoginTicket;
import org.example.Model.User;
import org.example.service.UserService;
import org.example.tools.CookiesUtil;
import org.example.tools.UserMsgStrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;


//创建拦截器对象，注入到容器
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private   UserService userService;

    @Autowired
    private UserMsgStrage userMsgStrage; //注入用户存贮对象

    //该拦截方法在Controller方法之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookiesUtil.getCookiesValue(request, "ticket");
        String servletPath = request.getServletPath();//服务路径
        if(ticket!=null) {
                LoginTicket loginTicket = userService.findLoginTicket(ticket);
                //检查登录凭证，状态，是否过期
                if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                    if(servletPath.startsWith("/quit")){ //如果是退出登录，就放行
                        return true;
                    }
                    //查询用户相关信息，并把用户信息存到一个线程隔离的工具类,如果是登录状态，就记录用户信息
                    User user = userService.FindUserById(loginTicket.getUserId());
                    userMsgStrage.setUser(user); //存贮用户信息

                }else if(servletPath.startsWith("/index")){ //访问首页放行

                      return true;
                }else if(servletPath.startsWith("/detail")) { //去看某条评论放行
                     return true;

                }else if(servletPath.startsWith("/profile")){
                    return true;
                }else if(servletPath.startsWith("/like")) {

                }else if(servletPath.startsWith("/search")){
                    return true;
                } else { //访问敏感资源，重定向
                    response.sendRedirect(request.getContextPath() + "/login");
                    return false;
                }
        }else if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                //判断是否是被拦截的方法
                LoginRequre loginRequired = method.getAnnotation(LoginRequre.class);
                if (loginRequired != null && userMsgStrage.getuser()==null) {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return false;
                }
        }else {
                //连凭证都没有,需要进一步判断，是否是访问敏感资源
                if(servletPath.startsWith("/index")){ //首页放行
                    return true;
                }else if(servletPath.startsWith("/detail")) { //去看某条评论放行
                    return true;
                } else if(servletPath.startsWith("/profile")) {
                    return true;
                } else {
                    //访问敏感资源，重定向到登录页
                    response.sendRedirect(request.getContextPath() + "/login");
                    return false;
                }
        }
        return true;
    }


    //该拦截方法在Controller之后调用，在返回视图之前调用
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = userMsgStrage.getuser();
        if(user!=null && modelAndView!=null){
            modelAndView.addObject("loginUser",user); //数据存到视图
        }
    }

    //返回视图之后执行,请求结束之前
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        userMsgStrage.clear(); //清理本次请求中的数据
    }
}
