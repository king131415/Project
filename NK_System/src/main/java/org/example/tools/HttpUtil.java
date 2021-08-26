package org.example.tools;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

public class HttpUtil {
    public static String readBody(HttpServletRequest req) throws IOException {
        //获取请求内容的长度
        int contentLength = req.getContentLength();
        //创建相应大小的数组
        byte[] buf=new byte[contentLength];
        try(InputStream inputStream=req.getInputStream()){
            inputStream.read(buf);
        }catch (IOException e){
            e.printStackTrace();
        }
        return new String(buf,"utf-8");
    }
}
