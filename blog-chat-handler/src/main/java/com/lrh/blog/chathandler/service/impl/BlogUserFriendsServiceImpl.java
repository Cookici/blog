package com.lrh.blog.chathandler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lrh.blog.chathandler.service.BlogUsersServer;
import com.lrh.blog.chathandler.utils.RedisUtils;
import com.lrh.blog.common.entity.BlogUserFriends;
import com.lrh.blog.chathandler.mapper.BlogUserFriendsMapper;
import com.lrh.blog.chathandler.service.BlogUserFriendsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.utils.RedisPrefix;
import com.lrh.blog.common.utils.RedisPrefixUtils;
import com.lrh.blog.common.vo.BlogUserFriendsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrh
 * @since 2023-11-09
 */
@Slf4j
@Service
public class BlogUserFriendsServiceImpl extends ServiceImpl<BlogUserFriendsMapper, BlogUserFriends> implements BlogUserFriendsService {

    @Autowired
    private BlogUsersServer blogUsersServer;

    @Autowired
    private BlogUserFriendsMapper blogUserFriendsMapper;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    @Transactional
    public Integer addFriend(BlogUserFriendsVo blogUserFriendsVo) {
        BlogUserFriends blogUserFriends = new BlogUserFriends();
        blogUserFriends.setUserId(blogUserFriendsVo.getUserId());
        blogUserFriends.setUserFriendsId(blogUserFriendsVo.getFriendId());
        blogUserFriends.setUserFriendsStatus("0");
        Result<BlogUsers> friend = blogUsersServer.getUserById(blogUserFriendsVo.getFriendId());
        blogUserFriends.setUserFriendsNote(friend.getData().getUserNickname());
        int insertUser = baseMapper.insert(blogUserFriends);

        blogUserFriends.setUserFriendsId(blogUserFriendsVo.getUserId());
        blogUserFriends.setUserId(blogUserFriendsVo.getFriendId());
        blogUserFriends.setUserFriendsStatus("0");
        Result<BlogUsers> user = blogUsersServer.getUserById(blogUserFriendsVo.getUserId());
        blogUserFriends.setUserFriendsNote(user.getData().getUserNickname());
        int insertFriend = baseMapper.insert(blogUserFriends);

        redisUtils.hdel("add" + blogUserFriendsVo.getUserId(), String.valueOf(blogUserFriendsVo.getFriendId()));

        return insertFriend + insertUser;
    }

    @Override
    @Transactional
    public Integer judgeHaveFriend(Long userId, String friendName) {
        Result<BlogUsers> blogUser = blogUsersServer.getByUserName(friendName);
        log.info("judge blogUser => " + blogUser.getData());
        Long friendId = blogUser.getData().getUserId();
        Integer byUserIdAndFriendId = blogUserFriendsMapper.getByUserIdAndFriendId(userId, friendId);
        if (byUserIdAndFriendId == 1) {
            redisUtils.hdel("add" + userId, String.valueOf(friendId));
        }
        return byUserIdAndFriendId;
    }

    @Override
    public Integer rejectApply(BlogUserFriendsVo blogUserFriendsVo) {
        redisUtils.hdel("add" + blogUserFriendsVo.getUserId(), String.valueOf(blogUserFriendsVo.getFriendId()));
        return 1;
    }

    @Override
    public List<BlogUsers> getFriendList(Long userId) {
        List<BlogUserFriends> blogUserFriendsList = baseMapper.selectList(new LambdaQueryWrapper<BlogUserFriends>().eq(BlogUserFriends::getUserId, userId));
        log.info("blogUserFriendsList: {}", blogUserFriendsList);
        List<Long> ids = new ArrayList<>();
        for (BlogUserFriends blogUserFriends : blogUserFriendsList) {
            ids.add(blogUserFriends.getUserFriendsId());
        }
        log.info("friendIds: {}", ids);
        Result<List<BlogUsers>> byIds = blogUsersServer.getByIds(ids);
        List<BlogUsers> data = byIds.getData();
        for (BlogUsers datum : data) {
            datum.setUserPassword(null);
            datum.setUserTelephoneNumber(null);
        }
        return data;
    }

    @Override
    public BlogUsers getFriend(Long friendId) {
        Result<BlogUsers> userById = blogUsersServer.getUserById(friendId);
        return userById.getData();
    }

    @Override
    @Transactional
    public Integer deleteFriend(BlogUserFriendsVo blogUserFriendsVo) {
        int delete = blogUserFriendsMapper.delete(new LambdaQueryWrapper<BlogUserFriends>().eq(BlogUserFriends::getUserId, blogUserFriendsVo.getUserId()).eq(BlogUserFriends::getUserFriendsId, blogUserFriendsVo.getFriendId()));
        int delete1 = blogUserFriendsMapper.delete(new LambdaQueryWrapper<BlogUserFriends>().eq(BlogUserFriends::getUserId, blogUserFriendsVo.getFriendId()).eq(BlogUserFriends::getUserFriendsId, blogUserFriendsVo.getUserId()));
        if (redisUtils.hasKey(RedisPrefixUtils.getStringBuilder(String.valueOf(blogUserFriendsVo.getUserId()), String.valueOf(blogUserFriendsVo.getFriendId()), RedisPrefix.SINGLE_CHAT_ONLINE).toString())) {
            redisUtils.del(RedisPrefixUtils.getStringBuilder(String.valueOf(blogUserFriendsVo.getUserId()), String.valueOf(blogUserFriendsVo.getFriendId()), RedisPrefix.SINGLE_CHAT_ONLINE).toString());
        }
        if (redisUtils.hasKey(RedisPrefixUtils.getStringBuilder(String.valueOf(blogUserFriendsVo.getUserId()), String.valueOf(blogUserFriendsVo.getFriendId()), RedisPrefix.SINGLE_CHAT_OFFLINE).toString())) {
            redisUtils.del(RedisPrefixUtils.getStringBuilder(String.valueOf(blogUserFriendsVo.getUserId()), String.valueOf(blogUserFriendsVo.getFriendId()), RedisPrefix.SINGLE_CHAT_OFFLINE).toString());
        }
        if (redisUtils.hasKey(RedisPrefixUtils.getStringBuilder(String.valueOf(blogUserFriendsVo.getUserId()), String.valueOf(blogUserFriendsVo.getFriendId()), RedisPrefix.RED_POINT).toString())) {
            redisUtils.del(RedisPrefixUtils.getStringBuilder(String.valueOf(blogUserFriendsVo.getUserId()), String.valueOf(blogUserFriendsVo.getFriendId()), RedisPrefix.RED_POINT).toString());
        }
        return delete + delete1;
    }

}
