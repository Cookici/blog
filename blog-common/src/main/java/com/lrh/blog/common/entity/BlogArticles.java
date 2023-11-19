package com.lrh.blog.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lrh
 * @since 2023-10-31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @TableField(exist = false)
    private BlogUsers blogUsers;

    @TableField(exist = false)
    private BlogLabels blogLabels;

    @TableField(exist = false)
    private BlogSorts blogSorts;

    public BlogArticles(Long articleId, Long userId, String articleTitle, String articleContent, Long articleViews, Long articleCommentCount, LocalDateTime articleDate, Long articleLikeCount) {
        this.articleId = articleId;
        this.userId = userId;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.articleViews = articleViews;
        this.articleCommentCount = articleCommentCount;
        this.articleDate = articleDate;
        this.articleLikeCount = articleLikeCount;
    }

}
