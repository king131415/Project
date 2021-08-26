package org.example.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class LoginTicket {

    //主键
    private int id;

    //用户Id
    private int userId;

    //登录凭证
    private String ticket;

    //状态 0:表示正常有效    1:过期无效
    private  int status;

    //过期时间
    private Date  expired;


}
