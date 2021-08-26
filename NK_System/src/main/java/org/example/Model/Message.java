package org.example.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString   //私信实体表
public class Message {

    private int id;

    private int fromId; //消息的发送者

    private int toId; //消息的接收者

    private String conversationId; //会话的ID

    private String content;  //内容

    private int status; //状态  0:未读   1:已读 2:删除

    private Date createTime; //创建时间

}
