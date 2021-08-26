package org.example.service;

import org.example.Mapper.MessageMapper;
import org.example.Model.Message;
import org.example.tools.SesitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    private SesitiveFilter sesitiveFilter;

    //查询当前用户的所有会话列表中的最新的一条消息
    public List<Message> findConversation(int userId,int start,int limit){
        return messageMapper.selectConversation(userId,start,limit);

    }
    //查询当前用户的会话数(和别人的私信）
    public int findConversationCount(int userId){
        return messageMapper.selectConvsersationCount(userId);
    }
    //查询当前用户的某个会话的所有私信
    public List<Message> findLetters(String conversationId,int start,int limit){
        return messageMapper.selectLetter(conversationId,start,limit);
    }
    //查询当前用户某个会话的私信总数
    public int findLeterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }
    //查询未读取私信
    public int findUnreaderLetter(int userId,String conversationId){
        return messageMapper.selectLetterUnreader(userId,conversationId);
    }

    //添加一条消息私信
    public int addMessage(Message message){

        //过滤内容的特殊标签
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        //过滤敏感词
        message.setContent(sesitiveFilter.filterString(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int readerMessage(List<Integer> ids){
        //更改消息的状态
        return messageMapper.updateMessageStatus(ids,1);
    }

    //查询某个主题的最新通知
    public Message findLastNotice(int userId,String topic){
        return messageMapper.selectLastNotice(userId,topic);

    }
    //查询某个主题的通知数量
    public int findNOticeCount(int userId,String topic){
        return  messageMapper.selectNOticeCount(userId,topic);

    }
    //查询主题的未读的通知数量
    public int findNOticeUnreaderCount(int userId,String topic){
        return messageMapper.selectUnreaderNoticeCount(userId,topic);
    }

    //查询某个主题下的所有私信
    public List<Message> findNotices(int userId,String topic,int start,int limit){
        return messageMapper.selectNotices(userId,topic,start,limit);
        

    }

}
