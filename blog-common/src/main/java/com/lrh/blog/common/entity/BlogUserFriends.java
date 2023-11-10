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
 * @since 2023-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogUserFriends implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标识ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 好友ID
     */
    private Long userFriendsId;

    /**
     * 好友备注
     */
    private String userFriendsNote;

    /**
     * 好友状态
     */
    private String userFriendsStatus;

    /**
     * 逻辑删除
     */
    private Integer deleted;


}
