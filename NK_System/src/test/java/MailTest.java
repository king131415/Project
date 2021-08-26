import org.example.NkApplication;
import org.example.tools.MailSend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NkApplication.class)
public class MailTest {
    @Autowired
    private MailSend mailSend;

    //注入模板引擎对象
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public  void TestMailSend(){
        mailSend.sendMail("2454213632@qq.com","测试邮件发送","你好！");

    }
    @Test
    public  void  TestMailHtml(){
        //创建模板引擎传参对象
        Context context=new Context();
        //设置给模板的参数参数内容
        context.setVariable("username","邱琦");
       //调用模板引擎生成动态网页(将某个路径下的Html,和参数内容传给模板引擎，===>这样他就把网页生成一个字符串）
        String content= templateEngine.process("/mail/TestThyleafMail.html", context);
        System.out.println(content);

        mailSend.sendMail("2454213632@qq.com","HTML",content);

    }
}
