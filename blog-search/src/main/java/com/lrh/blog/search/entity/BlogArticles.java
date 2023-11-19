package com.lrh.blog.search.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lrh
 * @since 2023-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogArticles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 博文ID
     */
    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;

    /**
     * 发表用户ID
     */
    private Long userId;

    /**
     * 博文标题
     */
    private String articleTitle;

    /**
     * 博文内容
     */
    private String articleContent;

    /**
     * 浏览量
     */
    private Long articleViews;

    /**
     * 评论总数
     */
    private Long articleCommentCount;

    /**
     * 发表时间
     */
    private LocalDateTime articleDate;

    /**
     * 点赞数
     */
    private Long articleLikeCount;

    /**
     * 逻辑删除
     */
    private Integer deleted;


}
