package com.lrh.blog.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lrh
 * @since 2023-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogComments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    /**
     * 发表用户ID
     */
    private Long userId;

    /**
     * 评论博文ID
     */
    private Long articleId;

    /**
     * 点赞数
     */
    private Long commentLikeCount;

    /**
     * 评论日期
     */
    private LocalDateTime commentDate;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论图片
     */
    private String commentContentImg;

    /**
     * 父评论ID
     */
    private Long parentCommentId;

    /**
     * 逻辑删除
     */
    private Integer deleted;


    @TableField(exist = false)
    private BlogUsers blogUsers;

    @TableField(exist = false)
    private List<BlogComments> children;

}
