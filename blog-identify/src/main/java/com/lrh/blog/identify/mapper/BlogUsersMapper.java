package com.lrh.blog.identify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lrh.blog.common.entity.BlogUsers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lrh
 * @since 2023-10-21
 */
@Mapper
public interface BlogUsersMapper extends BaseMapper<BlogUsers> {
    @Update({"update blog.blog_users set user_profile_photo=#{url} where user_id=#{id}"})
    Integer updateUrlById(@Param("id") Long id, @Param("url") String photoUrl);
}
