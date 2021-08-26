package org.example.tools;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookiesUtil {

    //每次请求都会携带Cookie,所以写个Cookie的工具类

    public static String getCookiesValue(HttpServletRequest request,String name){
           if(request==null || name==null){
               throw  new IllegalArgumentException("参数为空！！");
           }
         Cookie[] cookies = request.getCookies();
           if(cookies!=null){
               for(Cookie cookie:cookies){
                   if(cookie.getName().equals(name)){
                       return cookie.getValue();
                   }
               }
           }

           return null;

    }

}
