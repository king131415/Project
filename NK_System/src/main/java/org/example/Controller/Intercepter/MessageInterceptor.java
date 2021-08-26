package org.example.Controller.Intercepter;

import org.example.Model.User;
import org.example.service.MessageService;
import org.example.tools.UserMsgStrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MessageInterceptor  implements HandlerInterceptor {

    @Autowired
    private UserMsgStrage userMsgStrage;

    @Autowired
    private MessageService messageService;

    //Controller之后都去查询消息，用于统计头部页面消息
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user=userMsgStrage.getuser();
        if(user != null && modelAndView != null){
            //私信
            int letterUnreadCount=messageService.findUnreaderLetter(user.getId(),null);
            //通知
            int noticeUnreadCount=messageService.findNOticeUnreaderCount(user.getId(),null);

            modelAndView.addObject("allUnreadCount",letterUnreadCount+noticeUnreadCount);


        }
    }
}
