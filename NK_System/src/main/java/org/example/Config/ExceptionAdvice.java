package org.example.Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Model.ResposePostAndMesg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @ControllerAdvice(annotations = Controller.class) ==>表示作用于所有的Controller
 */
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {


    private static Gson gson=new GsonBuilder().create();

    private static ResposePostAndMesg p=new ResposePostAndMesg();


    private static final Logger logger=LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
         logger.error("服务器发生异常:"+e.getMessage());
         //详细记录异常信息
         for(StackTraceElement element:e.getStackTrace()){
             logger.error(element.toString());
         }
        String requestHeader = request.getHeader("x-requested-with");
         if("XMLHttpRequest".equals(requestHeader)){ //异步请求
             response.setContentType("application/json;charset=utf-8");
             PrintWriter writer = response.getWriter();
             p.setCode(1);
             p.setMsg("服务器异常！");
             String s = gson.toJson(p);
             writer.write(s);
         }else { //普通请求
             response.sendRedirect(request.getContextPath()+"/error");
         }
    }
}
