package com.lrh.blog.chathandler.mapper;

import com.lrh.blog.common.entity.BlogUserFriends;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lrh
 * @since 2023-11-09
 */
@Mapper
public interface BlogUserFriendsMapper extends BaseMapper<BlogUserFriends> {

    @Select("select count(*) from blog.blog_user_friends where user_id = #{userId} and user_friends_id = #{userFriendsId}")
    Integer getByUserIdAndFriendId(@Param("userId") Long userId,@Param("userFriendsId") Long friendId);
}
