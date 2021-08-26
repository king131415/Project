package org.example.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.Model.Message;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface MessageMapper {

    //查询当前用户的会话列表，针对每个会话只返回一条最新的数据
    List<Message> selectConversation(int userId, int start, int limit);

    //查询当前用户的会话数量
    int selectConvsersationCount(int userId);

    //查询某个会话包含的私信列表
    List<Message> selectLetter(String conversationId, int start, int limit);

    //查询某个会话包含的私信数量
    int selectLetterCount(String conversationId);

    //查询未读私信的数量
    int selectLetterUnreader(int userId,String conversationId);

    //新增消息私信
    int insertMessage(Message message);

    //更改消息私信的状态
    int updateMessageStatus(List<Integer> ids,int status);


    //查询某个主题下的最新的通知
    Message selectLastNotice(int userId,String topic);

    //某个主题下所包含通知的数量
    int selectNOticeCount(int userId,String topic);


    //查询某个主题的未读通知数量
    int selectUnreaderNoticeCount(int userId,String topic);

    //查询某个主题所包含的通知列表
    List<Message> selectNotices(int userId,String topic,int start,int limit);



}
