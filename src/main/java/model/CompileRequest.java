package model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompileRequest {
    //请求题目的id
    private int id;
    //用户编辑好的代码
    private String code;
    
}
