package org.example.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 此类利用SpringAOP==>进行统一的日志记录
 */
@Component
@Aspect
public class ServiceLogAspect {

    private static final Logger logger=LoggerFactory.getLogger(ServiceLogAspect.class);

    //定义切点
    //第一个*:表示返回值;org.example.service表示包名;.*表示所有的类;再.*表示所有的方法;(..)表示方法参数
    //结合起来的意思是org.example.service包下所有的类上的所有方法的任何参数，不管返回值都是切点。
    @Pointcut("execution(* org.example.service.*.*(..))")
    public void pointcut(){

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        //利用框架的工具类(RequestContextHolder)拿到Request对象
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes==null){ //一个特殊的调用，不进行记录(比如消费者调用了Service方法，就不记录了，不是Controller方法就不记录了
            return;
        }
        HttpServletRequest request=attributes.getRequest();
        String ip = request.getRemoteHost();
        String now=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //通过joinPoint.getSignature().getDeclaringTypeName() 获取切点作用的类名 + joinPoint.getSignature().getName()获得切点的作用的方法名
        String target=joinPoint.getSignature().getDeclaringTypeName()+"."+ joinPoint.getSignature().getName();
        logger.info(String.format("用户[%s],在[%s],访问了[%s].",ip,now,target));

    }
}
