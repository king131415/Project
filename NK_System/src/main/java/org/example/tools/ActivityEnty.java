package org.example.tools;

public interface ActivityEnty {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS=0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT=1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAIL=2;

    /**
     * 默认状态的登录过期时间
     */
    int DEFUALT_EXTRAT_SECOND=3600*12;

    /**
     * 记住我的情况下的超时时间
     */
    int REMEBERME_EXTRA_SECOND=3600*24*7;  //七天


    /**
     * 实体类型:帖子
     */
    int ENTITY_TYPE_POST=1;

    /**
     * 实体类型:评论
     */
    int ENTITY_TYPE_COMMENT=2;

    /**
     * 实体类型:用户
     */
    int ENTITY_TYPE=3;

    /**
     * 主题：评论
     */
    String TOPIC_COMMENT="comment";
    /**
     * 主题:点赞
     */
    String TOPIC_LIKE="like";
    /**
     * 主题:关注
     */
    String TOPIC_FOLLOW="follow";

    /**
     * 主题：发帖
     */
    String TOPIC_SENDPOST="publish";
    /**
     * 主题:删贴
     */
    String TOPIC_DELETE="delete";

    /**
     * 系统用户Id
     */
    int SYSTEM_USER_ID=1;



}
