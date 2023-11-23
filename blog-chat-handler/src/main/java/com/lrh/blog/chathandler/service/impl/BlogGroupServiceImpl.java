package com.lrh.blog.chathandler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.blog.chathandler.service.BlogUsersServer;
import com.lrh.blog.chathandler.utils.RedisUtils;
import com.lrh.blog.common.domin.Message;
import com.lrh.blog.common.entity.BlogGroup;
import com.lrh.blog.chathandler.mapper.BlogGroupMapper;
import com.lrh.blog.chathandler.service.BlogGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.utils.RedisPrefix;
import com.lrh.blog.common.utils.RedisPrefixUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrh
 * @since 2023-11-14
 */
@Service
public class BlogGroupServiceImpl extends ServiceImpl<BlogGroupMapper, BlogGroup> implements BlogGroupService {

    @Autowired
    private BlogUsersServer blogUsersServer;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<BlogUsers> getUsersByGroupId(Long groupId) {
        List<BlogGroup> groupList = baseMapper.selectList(new LambdaQueryWrapper<BlogGroup>().eq(BlogGroup::getGroupId, groupId));
        List<Long> ids = new ArrayList<>();
        for (BlogGroup blogGroup : groupList) {
            ids.add(blogGroup.getUserId());
        }
        Result<List<BlogUsers>> byIds = blogUsersServer.getByIds(ids);
        return byIds.getData();
    }

    @Override
    @Transactional
    public Integer deleteGroup(Long groupId) {

        List<BlogGroup> groupList = baseMapper.selectList(new LambdaQueryWrapper<BlogGroup>().eq(BlogGroup::getGroupId, groupId));
        List<Long> ids = new ArrayList<>();
        for (BlogGroup blogGroup : groupList) {
            ids.add(blogGroup.getUserId());
        }

        for (Long id : ids) {
            String redPoint = RedisPrefix.RED_POINT + id + "-group" + groupId;
            if (redisUtils.hasKey(redPoint)) {
                redisUtils.del(redPoint);
            }
        }

        redisUtils.del(RedisPrefixUtils.groupPrefix(String.valueOf(groupId)));
        if (redisUtils.hasKey(RedisPrefixUtils.groupMessagePrefix(String.valueOf(groupId)))) {
            Map<Object, Object> hmget = redisUtils.hmget(RedisPrefixUtils.groupMessagePrefix(String.valueOf(groupId)));
            for (Map.Entry<Object, Object> map : hmget.entrySet()) {
                redisUtils.del((String) map.getKey());
            }
            redisUtils.del(RedisPrefixUtils.groupMessagePrefix(String.valueOf(groupId)));
        }
        redisUtils.del(RedisPrefixUtils.groupHistoryMessagePrefix(String.valueOf(groupId)));

        return baseMapper.delete(new LambdaQueryWrapper<BlogGroup>().eq(BlogGroup::getGroupId, groupId));
    }
}
