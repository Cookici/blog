package com.lrh.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrh.blog.article.mapper.BlogCommentLikeMapper;
import com.lrh.blog.article.service.BlogUsersServer;
import com.lrh.blog.common.domin.BlogComments2;
import com.lrh.blog.common.domin.BlogUsers2;
import com.lrh.blog.common.domin.Reply;
import com.lrh.blog.common.entity.BlogCommentLike;
import com.lrh.blog.common.entity.BlogComments;
import com.lrh.blog.article.mapper.BlogCommentsMapper;
import com.lrh.blog.article.service.BlogCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrh.blog.common.entity.BlogUsers;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.utils.PageUtils;
import com.lrh.blog.common.vo.BlogCommentLikeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lrh
 * @since 2023-11-21
 */
@Slf4j
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements BlogCommentsService {

    @Autowired
    private BlogUsersServer blogUsersServer;

    @Autowired
    private BlogCommentsMapper blogCommentsMapper;

    @Autowired
    private BlogCommentLikeMapper blogCommentLikeMapper;

    @Override
    public List<BlogComments2> getCommentsTree(Long articleId) {

        List<BlogComments> blogCommentsList = baseMapper.selectList(new LambdaQueryWrapper<BlogComments>().eq(BlogComments::getArticleId, articleId));
        if (blogCommentsList.isEmpty()) {
            return null;
        }
        List<BlogComments2> blogComments2List = addUserForBlogComment2(blogCommentsList);

        return blogComments2List.stream().filter(blogComments2 -> blogComments2.getParentId() == null)
                .peek(menu -> menu.setReply(new Reply(getChildrenForCurrentMenu(menu, blogComments2List))))
                .collect(Collectors.toList());

    }

    @Override
    public Integer addLike(BlogCommentLikeVo blogCommentLikeVo) {
        LocalDateTime now = LocalDateTime.now();
        BlogCommentLike blogCommentLike = new BlogCommentLike();
        blogCommentLike.setCommentId(blogCommentLikeVo.getCommentId());
        blogCommentLike.setCommentLikeId(null);
        blogCommentLike.setUserId(blogCommentLikeVo.getUserId());
        blogCommentLike.setCommentUpdateDate(now);
        blogCommentLike.setCommentCreateDate(now);
        int insert = blogCommentLikeMapper.insert(blogCommentLike);
        BlogComments blogComments = blogCommentsMapper.selectById(blogCommentLikeVo.getCommentId());
        log.info("blogComments --> {}", blogComments);
        long likes = 0;
        if (blogComments.getCommentLikeCount() == null) {
            likes += 1;
        } else {
            likes = blogComments.getCommentLikeCount() + 1;
        }
        blogComments.setCommentLikeCount(likes);
        int update = blogCommentsMapper.update(blogComments, new LambdaQueryWrapper<BlogComments>().eq(BlogComments::getCommentId, blogCommentLikeVo.getCommentId()));
        return insert + update;
    }

    @Override
    public Long lastId() {
        return blogCommentsMapper.getLastId();
    }

    @Override
    public List<BlogComments2> getPageList(Long parentId) {
        List<BlogComments> blogCommentsList = blogCommentsMapper.selectList(new LambdaQueryWrapper<BlogComments>().eq(BlogComments::getParentCommentId, parentId));
        return addUserForBlogComment2(blogCommentsList);
    }

    public List<BlogComments2> addUserForBlogComment2(List<BlogComments> blogCommentsList) {
        blogCommentsList.sort(Comparator.comparing(BlogComments::getCommentDate).reversed());
        List<Long> userIds = new ArrayList<>();
        for (BlogComments blogComments : blogCommentsList) {
            userIds.add(blogComments.getUserId());
        }
        Result<List<BlogUsers>> byIds = blogUsersServer.getByIds(userIds);
        List<BlogUsers> users = byIds.getData();

        for (BlogComments blogComments : blogCommentsList) {
            if (userIds.contains(blogComments.getUserId())) {
                BlogUsers blogUsers = users.stream()
                        .filter(user -> user.getUserId().equals(blogComments.getUserId())).findFirst().get();
                blogComments.setBlogUsers(blogUsers);
            }
        }

        List<BlogComments2> blogComments2List = new ArrayList<>();
        for (BlogComments blogComments : blogCommentsList) {
            BlogComments2 blogComments2 = getBlogComments2(blogComments);
            blogComments2List.add(blogComments2);
        }

        return blogComments2List;
    }

    /**
     * 把blogComments转换为blogComments2前端需要的格式
     *
     * @param blogComments
     * @return
     */
    private static BlogComments2 getBlogComments2(BlogComments blogComments) {
        BlogComments2 blogComments2 = new BlogComments2();
        blogComments2.setId(String.valueOf(blogComments.getCommentId()));
        blogComments2.setUid(String.valueOf(blogComments.getBlogUsers().getUserId()));
        //TODO blogComments2.setAddress() 通过User的Ip
        if (blogComments.getParentCommentId() == 0L) {
            blogComments2.setParentId(null);
        } else {
            blogComments2.setParentId(String.valueOf(blogComments.getParentCommentId()));
        }
        blogComments2.setCreateTime(blogComments.getCommentDate());
        blogComments2.setContent(blogComments.getCommentContent());
        blogComments2.setContentImg(blogComments.getCommentContentImg());
        blogComments2.setLikes(blogComments.getCommentLikeCount());
        BlogUsers2 blogUsers2 = new BlogUsers2();
        blogUsers2.setUsername(blogComments.getBlogUsers().getUserNickname());
        blogUsers2.setAvatar(blogComments.getBlogUsers().getUserProfilePhoto());
        blogUsers2.setLevel(blogComments.getBlogUsers().getUserLevel());
        blogUsers2.setHomeLink("/home/personCenter/" + blogComments.getUserId());
        blogComments2.setUser(blogUsers2);
        return blogComments2;
    }


    private List<BlogComments2> getChildrenForCurrentMenu(BlogComments2 root, List<BlogComments2> all) {


        return all
                .stream()
                .filter(blogComments -> Objects.equals(blogComments.getParentId(), root.getId()))
                .peek(menu -> menu.setReply(new Reply(getChildrenForCurrentMenu(menu, all))))
                .collect(Collectors.toList());
    }
    
    
    
    
}
