package Servlet;

import DAO.ProblemDao;
import Utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Problem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/problem")
public class ProblemSevlet extends HttpServlet {
    private Gson gson=new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setStatus(200);

        String id=req.getParameter("id");
        if(id==null ||id.equals("")){ 
            selectAll(resp);

        }else {
            selectOne(Integer.parseInt(id),resp);
        }
    }

    private void selectAll(HttpServletResponse resp) throws IOException {
        ProblemDao blemDao=new ProblemDao();
        List<Problem> problems=blemDao.selectAll();

        String respString=gson.toJson(problems);

        resp.getWriter().println(respString);
    }

    private void selectOne(int problemId, HttpServletResponse resp) throws IOException {
        ProblemDao blemdao=new ProblemDao();
        Problem problem=blemdao.selectOne(problemId);
        String respString=gson.toJson(problem);
        resp.getWriter().write(respString);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String body= HttpUtil.readBody(req);
        Problem problem=gson.fromJson(body,Problem.class);

        ProblemDao blemDao=new ProblemDao();
        blemDao.insert(problem);
        resp.setStatus(200);
        resp.getWriter().println("{\"ok\":1}");

    }
}
