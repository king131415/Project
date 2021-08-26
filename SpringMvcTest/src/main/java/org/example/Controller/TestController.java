package org.example.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    private static Gson gson=new GsonBuilder().create();

    @GetMapping("/test")
    public void Test(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("name","邱琦");
        map.put("age",19);
        map.put("hight",170);
        String s = gson.toJson(map);
       response.getWriter().write(s);

    }

}
