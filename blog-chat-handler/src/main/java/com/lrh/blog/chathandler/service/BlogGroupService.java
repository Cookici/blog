package com.lrh.blog.chathandler.service;

import com.lrh.blog.common.entity.BlogGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrh.blog.common.entity.BlogUsers;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lrh
 * @since 2023-11-14
 */
public interface BlogGroupService extends IService<BlogGroup> {

    List<BlogUsers> getUsersByGroupId(Long groupId);

    Integer deleteGroup(Long groupId);
}
