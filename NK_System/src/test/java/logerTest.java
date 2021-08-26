import org.example.NkApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NkApplication.class)
public class logerTest {

    //日志记录对象
    private static final Logger logger= LoggerFactory.getLogger(logerTest.class);



    @Test
    public void TestLoger(){
        System.out.println(logger.getName());
        //首先要去Properties配置文件中配置日志记录级别
        //还可以配置日志输出的位置

        logger.debug("debug bug!");
        logger.info("bug infof");
        logger.warn("WORN!!!!");
        logger.error("eeror bug"); //错误级别的日志
    }
}
