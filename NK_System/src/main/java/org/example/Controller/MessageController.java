package org.example.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.protocol.types.Field;
import org.example.Controller.Anotation.LoginRequre;
import org.example.Model.Message;
import org.example.Model.ResposePostAndMesg;
import org.example.Model.User;
import org.example.service.MessageService;
import org.example.service.UserService;
import org.example.tools.ActivityEnty;
import org.example.tools.Page;
import org.example.tools.UserMsgStrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Controller
public class MessageController implements ActivityEnty {

    private static Gson gson = new GsonBuilder().create();

    @Autowired
    private UserMsgStrage userMsgStrage;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;


    //私信列表
    @LoginRequre
    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page) {
        User user = userMsgStrage.getuser();
        //设置分页的信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));

        //查询
        List<Message> conversationList = messageService.findConversation(user.getId(), page.getStart(), page.getLimit());

        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLeterCount(message.getConversationId()));
                map.put("unreaderCount", messageService.findUnreaderLetter(user.getId(), message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.FindUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);
        //查询整个用户的未读消息
        int letterUnreaderCount = messageService.findUnreaderLetter(user.getId(), null);

        //查询用户未读取系统通知的数量
        int noticeUnreaderCount = messageService.findNOticeUnreaderCount(user.getId(), null);
        model.addAttribute("noticeUnreaderCount", noticeUnreaderCount);

        model.addAttribute("letterUnreaderCount", letterUnreaderCount);
        model.addAttribute("page", page);

        return "/site/letter";

    }



    //某个会话的私信详情
    @LoginRequre
    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        //分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLeterCount(conversationId));

        //某个会话的所有私信
        List<Message> letterList = messageService.findLetters(conversationId, page.getStart(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.FindUserById(message.getFromId()));
                letters.add(map);
            }
        }
        //查询私信的目标
        model.addAttribute("target", getLettertarget(conversationId));
        model.addAttribute("letters", letters);
        model.addAttribute("page", page);

        //设置已读
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readerMessage(ids);
        }
        return "/site/letter-detail";

    }

    private List<Integer> getLetterIds(List<Message> LetterList) {
        List<Integer> ids = new ArrayList<>();
        if (LetterList != null) {
            for (Message message : LetterList) {
                if (userMsgStrage.getuser().getId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }


    //根据会话查找私信的目标Id
    private User getLettertarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id1 = Integer.parseInt(ids[0]);
        int id2 = Integer.parseInt(ids[1]);
        if (userMsgStrage.getuser().getId() == id1) {
            return userService.FindUserById(id2);
        } else {
            return userService.FindUserById(id1);
        }
    }


    @LoginRequre
    @PostMapping("/letter/send")
    @ResponseBody    //表示返回值为字符串
    public String sendLetter(String toName, String content) {
        User target = userService.findUserByName(toName);
        ResposePostAndMesg respons = new ResposePostAndMesg();
        if (target == null) {
            respons.setCode(1);
            respons.setMsg("目标用户不存在！！");
            String s = gson.toJson(respons);
            return s;
        }

        Message m = new Message();
        m.setFromId(userMsgStrage.getuser().getId());
        m.setToId(target.getId());
        if (m.getFromId() < m.getToId()) {
            m.setConversationId(m.getFromId() + "_" + m.getToId());
        } else {
            m.setConversationId(m.getToId() + "_" + m.getFromId());
        }
        m.setContent(content);
        m.setCreateTime(new Date());
        messageService.addMessage(m);
        respons.setCode(0);
        respons.setMsg("发送成功！！");
        String s = gson.toJson(respons);
        return s;
    }

    //显示通知列表服务
    @LoginRequre
    @GetMapping("/notice/list")
    public String getNoticeList(Model model) {
        //查询当前用户，给当前用户的通知
        User user = userMsgStrage.getuser();

        //查询评论类的通知
        Message message = messageService.findLastNotice(user.getId(), TOPIC_COMMENT);
        if (message != null) {
            Map<String, Object> messageVo = new HashMap<>();
            messageVo.put("message", message);
            //反转义
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = gson.fromJson(content, HashMap.class);

            int userId = (int)((double)data.get("userId"));
            messageVo.put("user", userService.FindUserById(userId));
            messageVo.put("entityType", data.get("entityType"));
            messageVo.put("entityId", data.get("entityId"));
            messageVo.put("postId", data.get("postId"));

            int count = messageService.findNOticeCount(user.getId(), TOPIC_COMMENT);
            messageVo.put("count", count);

            int unread = messageService.findNOticeUnreaderCount(user.getId(), TOPIC_COMMENT);
            messageVo.put("unread", unread);
            model.addAttribute("commentNotice", messageVo);

        }


        //查询点赞类的通知
        message = messageService.findLastNotice(user.getId(), TOPIC_LIKE);
        if (message != null) {
            Map<String, Object> messageVo = new HashMap<>();
            messageVo.put("message", message);
            //反转义
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = gson.fromJson(content, HashMap.class);

            int userId = (int)((double)data.get("userId"));
            messageVo.put("user", userService.FindUserById(userId));

            messageVo.put("entityType", data.get("entityType"));
            messageVo.put("entityId", data.get("entityId"));
            messageVo.put("postId", data.get("postId"));

            int count = messageService.findNOticeCount(user.getId(), TOPIC_LIKE);
            messageVo.put("count", count);
            int unread = messageService.findNOticeUnreaderCount(user.getId(), TOPIC_LIKE);
            messageVo.put("unread", unread);
            model.addAttribute("likeNotice", messageVo);
        }


        //查询关注类的通知
        message = messageService.findLastNotice(user.getId(), TOPIC_FOLLOW);
        if (message != null) {
            Map<String, Object> messageVo = new HashMap<>();
            messageVo.put("message", message);
            //反转义
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = gson.fromJson(content, HashMap.class);

            int userId = (int)((double)data.get("userId"));
            messageVo.put("user", userService.FindUserById(userId));
            messageVo.put("entityType", data.get("entityType"));
            messageVo.put("entityId", data.get("entityId"));

            int count = messageService.findNOticeCount(user.getId(), TOPIC_FOLLOW);
            messageVo.put("count", count);
            int unread = messageService.findNOticeUnreaderCount(user.getId(), TOPIC_FOLLOW);
            messageVo.put("unread", unread);
            model.addAttribute("followNotice", messageVo);
        }


        //查询未读消息数量
        int letterUnreaderCount = messageService.findUnreaderLetter(user.getId(), null);
        model.addAttribute("letterUnreaderCount", letterUnreaderCount);
        //查询未读取的主题通知数量
        int noticeUnreaderCount = messageService.findNOticeUnreaderCount(user.getId(), null);
        model.addAttribute("noticeUnreaderCount", noticeUnreaderCount);

        return "/site/notice";
    }

    //某种通知的详情
    @GetMapping("/notice/detail/{topic}")
    @LoginRequre
    public String getNoticeDetail(@PathVariable("topic") String topic,Page page,Model model){
        User user=userMsgStrage.getuser();
        page.setLimit(5);
        page.setPath("/notice/detail/"+topic);
        page.setRows(messageService.findNOticeCount(user.getId(),topic));

        List<Message> noticeList=messageService.findNotices(user.getId(),topic,page.getStart(),page.getLimit());
        List<Map<String,Object>> noticeVoList=new ArrayList<>();
        if(noticeList!=null){
            for (Message notice:noticeList){
                Map<String,Object> map=new HashMap<>();
                map.put("notice",notice);
                String content=HtmlUtils.htmlUnescape(notice.getContent());
                Map<String,Object> data=gson.fromJson(content,HashMap.class);
                int userId=(int)((double)data.get("userId"));
                map.put("user",userService.FindUserById(userId));
                map.put("entityType",data.get("entityType"));
                map.put("entityId",data.get("entityId"));
                if(data.get("postId")!=null){
                    int postId = (int)((double)data.get("postId"));
                    map.put("postId",postId);

                }
                //通知的作者
                map.put("fromUser",userService.FindUserById(notice.getFromId()));
                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices",noticeVoList);
        //设置已读
        List<Integer> ids=getLetterIds(noticeList);
        if(!ids.isEmpty()){
            messageService.readerMessage(ids);
        }
        return "/site/notice-detail";
    }
}
