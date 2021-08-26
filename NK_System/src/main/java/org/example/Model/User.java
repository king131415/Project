package org.example.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
public class User {

    private int id;
    private  String username;
    private  String password;
    private  String salt; //加密用的字符串

    private String email;
    private int type; //用户类型 0-普通用户; 1-超级管理员; 2-版主;',
    private int status; //  '0-未激活; 1-已激活;',
    private String activationCode; //激活码
    private String headerUrl; //头像地址
    private Date createTime; //创建时间

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", headerUrl='" + headerUrl + '\'' +
                '}';
    }
}
