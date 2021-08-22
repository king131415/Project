package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompileResponse {
    //响应的错误吗
    private int errno;
    //原因
    private String reason;
    //输出
    private String stdout;
    
}
