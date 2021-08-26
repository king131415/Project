package org.example.tools;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class NK_Util {

    //生成随机字符串
    public static  String getUUiD(){
        return UUID.randomUUID().toString().replaceAll("-","");

    }
    //md5加密 :密码加盐在加密
    public  static String getPasswordMd5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        //基于spring框架生成md5加密字符串
        return DigestUtils.md5DigestAsHex(key.getBytes());

    }
}
