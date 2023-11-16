package com.lrh.blog.common.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lrh
 * @since 2023-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogSetArticleSort implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 分类ID
     */
    private Long sortId;


}
