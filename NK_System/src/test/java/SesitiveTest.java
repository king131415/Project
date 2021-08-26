import org.example.NkApplication;
import org.example.tools.SesitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Target;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NkApplication.class)
public class SesitiveTest {
    @Autowired
    private SesitiveFilter sesitiveFilter;

    @Test
    public  void  testSesitive(){
        String text="这里可以赌博，可以吸毒，可以嫖娼，可以开房>>>";
        String s = sesitiveFilter.filterString(text);
        System.out.println(s);

        String text1="这里可以👏赌₩博₩，可以吸毒，可以嫖娼，可以开房>>>";
        String s2 = sesitiveFilter.filterString(text1);
        System.out.println(s2);



    }
}
