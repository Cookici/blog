package com.lrh.blog.common.entity;

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
 * @since 2023-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogCommentLike implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "comment_like_id", type = IdType.AUTO)
    private Long commentLikeId;

    private Long userId;

    private Long commentId;

    private LocalDateTime commentCreateDate;

    private LocalDateTime commentUpdateDate;


}
