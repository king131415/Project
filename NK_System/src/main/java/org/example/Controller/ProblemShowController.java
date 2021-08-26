package org.example.Controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Controller.Anotation.LoginRequre;
import org.example.Mapper.UserMapper;
import org.example.Model.Problem;
import org.example.Model.User;
import org.example.service.ProblemShowService;
import org.example.tools.Page;
import org.example.tools.UserMsgStrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//@WebServlet("/problem")
@Controller
public class ProblemShowController {

    private static Gson gson=new GsonBuilder().create();

    private static Problem p=new Problem();

    @Autowired
    private   ProblemShowService problemService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    UserMsgStrage userMsgStrage;

    @LoginRequre
    @GetMapping("/problem")
    @ResponseBody
    public void getProblem(@RequestParam(required = false) Integer id,
                           @RequestParam(required = false) Integer strart,
                           HttpServletResponse resp) throws IOException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setStatus(200);

        if(id==null ||id.equals("")){
            List<Problem> problems=null;
            if(strart==null){

                problems = problemService.SelectAll(p.getStrart(),p.getLimit());


            }else {
                problems = problemService.SelectAll(strart, p.getLimit());
            }
            String respString=gson.toJson(problems);

            resp.getWriter().println(respString);

        }else {
            Problem problem = problemService.selectByOne(id);
            String respString=gson.toJson(problem);
            resp.getWriter().write(respString);

        }
    }
    @LoginRequre
    @GetMapping("/imge")
    public void getImge(@RequestParam(required = false) Integer id,
                        @RequestParam(required = false) Integer strart,
                        HttpServletResponse resp) throws IOException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setStatus(200);
        User user = userMsgStrage.getuser();

        String respString=gson.toJson(user);

        resp.getWriter().println(respString);
    }
}
