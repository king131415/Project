package org.example.tools;

public class RedisKeyUtil {
    private static final String SPLIT=":";

    //某个实体的赞,帖子，评论，回复
    private static final String PRE="like:entity";
    //某个用户的赞
    private static final String USER_LIKE_PRE="like:user";
    //某个用户关注的实体
    private static final String FOLLOWEE="followee";
    //某个用户的粉丝
    private static final String FOLLOER="follower";
   //验证码
    private static final String CHECK_CODE="kaptcha";
    //登录凭证
    private static final String LOGIN_TICKET="ticket";
   //用户
    private static final String CACHE_USER="user";



    //某个实体的赞
    //like:entity:entityType:entityId ==>userId
    public static String getEntityKey(int entityType,int entityId ){

        return PRE+SPLIT+entityType+SPLIT+entityId;
    }
    //某个用户的赞
    //like:user:userId ==>int
    public static String getUserLikeKey(int userId){
        return USER_LIKE_PRE+SPLIT+userId;
    }


    //某个用户关注的实体
    //followee:userId:entityType ==>Zset(entityId,now)
    public static String getFolloweeKey(int userId,int entityType){
        return FOLLOWEE+SPLIT+userId+SPLIT+entityType;
    }

   //某个实体拥有的粉丝
    //follower:entityType:entityId ==>Zset(userId,now)
   public static String getFolloerKey(int entityType,int entityId){
        return FOLLOER+SPLIT+entityType+SPLIT+entityId;
   }

    //登录验证码
    public static String getKaptchKey(String Owner){
        return  CHECK_CODE+SPLIT+Owner;
    }
    //登录的凭证
    public static String getLoginTicket(String ticket){
        return LOGIN_TICKET+SPLIT+ticket;
    }

    //获取用户
    public static String getUserKey(int userId){
        return CACHE_USER+SPLIT+userId;
    }

}
