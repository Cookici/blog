package com.lrh.blog.chathandler.service.impl;

import com.lrh.blog.chathandler.service.BlogUsersServer;
import com.lrh.blog.common.entity.BlogUserFriends;
import com.lrh.blog.chathandler.mapper.BlogUserFriendsMapper;
import com.lrh.blog.chathandler.service.BlogUserFriendsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.vo.BlogUserFriendsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrh
 * @since 2023-11-09
 */
@Service
public class BlogUserFriendsServiceImpl extends ServiceImpl<BlogUserFriendsMapper, BlogUserFriends> implements BlogUserFriendsService {

    @Autowired
    private BlogUsersServer blogUsersServer;

    @Override
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
        return insertFriend + insertUser;
    }
}
