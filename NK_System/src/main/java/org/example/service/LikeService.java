package org.example.service;

import org.aspectj.lang.annotation.Aspect;
import org.example.tools.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    //点赞,一次业务有几次数据访问的话，要进行事务的管理
    public void like(int userId,int entityType,int entityId,int entityUserId){
       //事务统一处理
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations Operations) throws DataAccessException {
                String entityLikeKey= RedisKeyUtil.getEntityKey(entityType,entityId);
                String userLikeKey=RedisKeyUtil.getUserLikeKey(entityUserId);
                //当前用户有没有对实体点过赞
                boolean isMeber=Operations.opsForSet().isMember(entityLikeKey,userId);

                Operations.multi(); //开启事务
                if(isMeber){//若存在，说明点过赞了，就是取消赞
                    Operations.opsForSet().remove(entityLikeKey,userId);
                    //记录某个用户的点赞数(减一）
                    Operations.opsForValue().decrement(userLikeKey);
                }else {//没点过赞，就点赞
                    Operations.opsForSet().add(entityLikeKey,userId);
                    //记录某个用户的点赞数(加一）
                    Operations.opsForValue().increment(userLikeKey);
                }

                return  Operations.exec(); //执行事务
            }
        });
    }

    //查询某个用户的赞的数量
    public int findUserLikeCount(int userId){
        String userLikeKey=RedisKeyUtil.getUserLikeKey(userId);
        Integer count=(Integer)redisTemplate.opsForValue().get(userLikeKey);
        return count==null?0:count.intValue();
    }

    //查询某实体点赞的数量
    public long findEntityLikeCount(int entityType,int entityId){
        String entityLikeKey= RedisKeyUtil.getEntityKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId,int entityType,int entityId){
        String entityLikeKey= RedisKeyUtil.getEntityKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1 : 0;
    }
}
