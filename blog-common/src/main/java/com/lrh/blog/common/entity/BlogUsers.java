package com.lrh.blog.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author lrh
 * @since 2023-10-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BlogUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户IP
     */
    private String userIp;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户头像
     */
    private String userProfilePhoto;

    /**
     * 注册时间
     */
    private LocalDateTime userRegistrationTime;

    /**
     * 用户生日
     */
    private LocalDate userBirthday;

    /**
     * 用户年龄
     */
    private Integer userAge;

    /**
     * 用户手机号
     */
    private String userTelephoneNumber;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户等级
     */
    private Integer userLevel;

    /**
     * 权限
     */
    private String userAuthority;

    /**
     * 登录状态
     */
    private Integer userStatus;

}
