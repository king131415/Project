package org.example.Controller;


import org.example.Controller.Anotation.LoginRequre;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class StudyController {

    @LoginRequre
    @GetMapping("/study")
    public void getStudyPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.sendRedirect(request.getContextPath()+"/study.html");
    }
}
