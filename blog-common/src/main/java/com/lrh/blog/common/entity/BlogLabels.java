package com.lrh.blog.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class BlogLabels implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签ID
     */
    @TableId(value = "label_id", type = IdType.AUTO)
    private Long labelId;

    /**
     * 标签名称
     */
    private String labelName;

    /**
     * 标签别名
     */
    private String labelAlias;

    /**
     * 标签描述
     */
    private String labelDescription;


}
