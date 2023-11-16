package com.lrh.blog.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

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
 * @since 2023-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogSorts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "sort_id", type = IdType.AUTO)
    private Long sortId;

    /**
     * 分类名称
     */
    private String sortName;

    /**
     * 分类别名
     */
    private String sortAlias;

    /**
     * 分类描述
     */
    private String sortDescription;

    /**
     * 父分类ID
     */
    private Long parentSortId;


    @TableField(exist = false)
    private List<BlogSorts> children;


}
