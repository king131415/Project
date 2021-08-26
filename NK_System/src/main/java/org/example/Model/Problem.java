package org.example.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Problem {  //实体类，这个实体类就对应数据库的一条记录
    private int id;
    private String title; //题目的标题
    private String level;  //难度
    private String description; //描述
    private String templatecode;  //和前端相呼应，模板代码
    private String testCode;  //测试代码




    private int  strart=0;

    private int limit=6;

}
