package compile;

import Utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
//用这个类来表示一次完整的编译运行的过程
public class Task {
    //所有的临时文件要放到这个目录中
    private String WORK_DIR;
    //要编辑执行的类的名字，影响到源代码的文件名
    private String CLASS="Solution";
    //要编译执行的文件名
    private String CODE;
    //程序标准输出放置的位置
    private String STDOUT;
    //程序标准错误放置的位置
    private String STDERR;
    //程序的编译出错存放的文件
    private  String COMPILE_ERROR;

    public Task() {
       WORK_DIR="./tmp/"+ UUID.randomUUID().toString()+"/";
       CODE=WORK_DIR+CLASS+".java";
       STDOUT=WORK_DIR+"stdout.txt";
       STDERR=WORK_DIR+"stderr.txt";
       COMPILE_ERROR=WORK_DIR+"compile_error.txt";
    }
    public Answer complieAndRun(Question question) throws IOException, InterruptedException {
        Answer answer=new Answer();
        File file=new File(WORK_DIR);
        if(!file.exists()){
            file.mkdirs();
        }
        //准备好要编译的源代码文件
        FileUtil.writeFile(CODE,question.getCode());
        //构造预编译指令
        //  // 2. 构造编译指令(javac), 并进行执行. 预期得到的结果
        //        //    就是一个对应的 .class 文件, 以及编译出错的文件
        //        //    -d 表示 生成的.class文件放置的位置
        //        //    javac -encoding utf-8 ./tmp/Solution.java -d ./tmp/
        //        // String compileCmd = "javac -encoding utf-8 " + CODE + " -d " + WORK_DIR;
        //        // String.format 类似于 C 中的 sprintf
        String compileCmd=String.format("javac -encoding utf-8 %s -d %s",CODE,WORK_DIR);

//        System.out.println(compileCmd); //打印编辑命令

       //执行预编译指令
        CommandUtil.run(compileCmd,null,COMPILE_ERROR);

        String compileError=FileUtil.readFile(COMPILE_ERROR);
        if(!compileError.equals("")){
            answer.setErrno(1);
            answer.setReason(compileError);
            return answer;
        }
        //构造编译命令
        //  就是这个代码的标准输出的文件和标准错误的文件
        //        //    为了让 java 命令找到 .class 文件的位置, 还需要加上一个选项
        //        //    -classpath 通过这个选项来执行 .class 文件放到哪个目录里了
        //        //    java Solution
        String runCmd=String.format("java -classpath %s %s",WORK_DIR,CLASS);
//        System.out.println(runCmd);
       //执行编译命令
       CommandUtil.run(runCmd,STDOUT,STDERR);

        String runError=FileUtil.readFile(STDERR);
        if(!runError.equals("")){
            answer.setErrno(2);
            answer.setReason(runError);
            return answer;
        }
        answer.setErrno(0);
        String runStdout=FileUtil.readFile(STDOUT);
        answer.setStdout(runStdout);
        return answer;

    }

    public static void main(String[] args) {
        Task task=new Task();
        Question question=new Question();
        
    }
}
