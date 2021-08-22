package Servlet;



import DAO.ProblemDao;
import Utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import compile.Answer;
import compile.Question;
import compile.Task;
import model.CompileRequest;
import model.CompileResponse;
import model.Problem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/compile")  ///compile
public class ComplieSevlet extends HttpServlet {
    private Gson gson=new GsonBuilder().create();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        //拿到请求的内容
        String body= HttpUtil.readBody(req);
        //把Json对象序列化成Java对象
        CompileRequest compileRequest=gson.fromJson(body,CompileRequest.class);

        ProblemDao problm=new ProblemDao();
        Problem problem=problm.selectOne(compileRequest.getId());
        //获取测试代码
        String testCode=problem.getTestCode();
       //获取请求中的代码
        String requestCode = compileRequest.getCode();
        //最终进行代码拼接
        String finalCode=merge(requestCode,testCode);


        Task task=new Task();

        Question question=new Question();
        
        question.setCode(finalCode);

        Answer answer=null;

        try {
            answer=task.complieAndRun(question);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CompileResponse compileResponse=new CompileResponse();
        compileResponse.setErrno(answer.getErrno());
        compileResponse.setReason(answer.getReason());
        compileResponse.setStdout(answer.getStdout());
        String respString=gson.toJson(compileResponse);
        resp.setStatus(200);
        resp.getWriter().println(respString);


    }

    private String merge(String requestCode, String testCode) {
        int pos=requestCode.lastIndexOf("}");
        if(pos==-1){    //防止特殊情况
            return null;
        }
        //拼接字符串
        return requestCode.substring(0,pos)+testCode+"}";
    }
}
