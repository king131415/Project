package org.example.tools;


import org.example.Model.User;
import org.springframework.stereotype.Component;

// 存放用户信息，代替Session
@Component
public class UserMsgStrage {

    //线程隔离的类，用于存放用户信息
    private ThreadLocal<User> users=new ThreadLocal<>();

    //存
    public void  setUser(User user){
        users.set(user);
    }
    //取
    public  User getuser(){
        return users.get();
    }

    //清理ThreadLocal中的数据，每次请求结束后

    public void clear(){
        users.remove();
    }




}
