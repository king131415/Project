package org.example.service;

import org.example.Model.User;
import org.example.tools.ActivityEnty;
import org.example.tools.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowService  implements ActivityEnty {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;


    //关注
    public void follow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
               String followeeKey= RedisKeyUtil.getFolloweeKey(userId,entityType);
               String followerKey=RedisKeyUtil.getFolloerKey(entityType,entityId);

               operations.multi();//启动事务

                operations.opsForZSet().add(followeeKey,entityId,System.currentTimeMillis());

                operations.opsForZSet().add(followerKey,userId,System.currentTimeMillis());


                return operations.exec(); //执行事务
            }
        });
    }

    //取消关注
    public void Unfollow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey= RedisKeyUtil.getFolloweeKey(userId,entityType);
                String followerKey=RedisKeyUtil.getFolloerKey(entityType,entityId);

                operations.multi();//启动事务

                operations.opsForZSet().remove(followeeKey,entityId);

                operations.opsForZSet().remove(followerKey,userId);


                return operations.exec(); //执行事务
            }
        });
    }

    //查询关注的实体的数量
    public  long findFolloweeCount(int userId,int entityType ){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);

    }
    //查询实体的粉丝的数量
    public long findFollowerCount(int entityType,int entityId){
        String followerKey=RedisKeyUtil.getFolloerKey(entityType,entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    //查询当前用户是否已经关注某实体
    public boolean hasFollowed(int userId,int entityType,int entityId){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().score(followeeKey,entityId)!=null;

    }

    //查询某个用户关注的人
    public List<Map<String,Object>> findFollowees(int userId,int Start,int limit){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, Start, Start + limit - 1);
        if(targetIds==null){
            return null;
        }
        List<Map<String,Object>> list=new ArrayList<>();
        for(Integer targetId:targetIds){
            Map<String,Object> map=new HashMap<>();
            //根据id==>找到用户实体，并把用户实体的所有信息放到Map里面去
            User user= userService.FindUserById(targetId);
            map.put("user",user);
            //分数就是当前时间，把时间也放进去Map
            Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);
            map.put("followTime",new Date(score.longValue()));
            list.add(map);
        }

        return list;

    }

    //查询某用户的粉丝
    public List<Map<String,Object>> findFollowers(int userId,int start,int limit){
        String followerKey=RedisKeyUtil.getFolloerKey(ENTITY_TYPE,userId);
        Set<Integer> targetIds=redisTemplate.opsForZSet().reverseRange(followerKey,start,start+limit-1);

        if(targetIds==null){
            return null;
        }
        List<Map<String,Object>> list=new ArrayList<>();
        for(Integer targetId:targetIds){
            Map<String,Object> map=new HashMap<>();
            User user= userService.FindUserById(targetId);
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
            map.put("followTime",new Date(score.longValue()));
            list.add(map);
        }
        return list;

    }

}