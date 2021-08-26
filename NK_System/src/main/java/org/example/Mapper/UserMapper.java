package org.example.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.Model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Mapper
@Component
public interface UserMapper {
    //根据id查用户
    User selectById(int id);

    User selectByName(String username);

    User selctByEmail(String email);


    //增加用户

    int insertUser(User user);

    //修改用户的状态

    int updateStatus(int id,int status);

    //更行图像的路径
    int updateHeader(int id,String headerUrl);

    //更改密码

    int updatePassword(int id,String password);






}
