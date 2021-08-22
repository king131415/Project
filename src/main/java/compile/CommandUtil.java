package compile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * 通过这个类来创建进程，并执行javac,java等命令
 */
public class CommandUtil {
    public static int run(String cmd,String stdoutFile,String stderrFile) throws IOException, InterruptedException {
        //创建一个进程来执行编译等命令，process表示你新创建出来的进程对应的实例对象，其父进程就是当前运行的进程
        Process process=Runtime.getRuntime().exec(cmd);
        if(stdoutFile!=null){
            //process.getInputStream()用于获取子进程的正常输出的输入流,对于我们当前程序来说是输入流
            //process.getOutputStream()用户获取子进程正常输入的输出流
            InputStream stdoutFrom=process.getInputStream();
            FileOutputStream stdoutTo=new FileOutputStream(stdoutFile);
            while (true){
                int ch=stdoutFrom.read();
                if(ch==-1){
                    break;
                }
                stdoutTo.write(ch);
            }
            //关闭流，释放资源
            stdoutFrom.close();
            stdoutTo.close();
        }
        if(stderrFile!=null){
            InputStream stderrFrom=process.getErrorStream();
            FileOutputStream stderrTo=new FileOutputStream(stderrFile);
            while (true){
                int ch=stderrFrom.read();
                if(ch==-1){
                    break;
                }
                stderrTo.write(ch);
            }
            stderrFrom.close();
            stderrTo.close();
        }
        //等待新进程结束，并且获得到退出码
        int exitCode=process.waitFor();
        return exitCode;
    }

    public static void main(String[] args) {
        try {
         int ret=CommandUtil.run("javac","./stdout.txt","./stderr.txt");
            System.out.println(ret);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
