package com.lrh.blog.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author lrh
 * @since 2023-11-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BlogArticlesLike implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "article_like_id", type = IdType.AUTO)
    private Long articleLikeId;

    private Long articleId;

    private Long userId;

    private LocalDateTime articleLikeCreateDate;

    private LocalDateTime articleLikeUpdateDate;


}
