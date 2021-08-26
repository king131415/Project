package org.example.Mapper;


import org.apache.ibatis.annotations.*;
import org.example.Model.LoginTicket;
import org.springframework.stereotype.Repository;

/**
 * 已经用Redis代替  @Deprecated ==>声明该组件已经不推荐使用
 */
@Deprecated
@Repository
@Mapper
public interface LoginTicketMapper {

     //生成一条凭证插入数据库
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);



    //生成凭证插入数据库，后会把凭证设置到Cookie中返回给浏览器，
    //以后浏览器携带凭证，就可以拿到整个用户登录状态记录表的一条唯一记录
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket  selectByLoginTicket(String ticket);



    //根据用户Id查询
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where user_id=#{userId}"
    })
    LoginTicket  selecrByLogginTicket(int userId);




    //修改状态
    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateLoginTicket(String ticket,int status);
}
