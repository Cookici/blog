package com.lrh.blog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.vo
 * @ClassName: BlogUserFriendsVo
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/10 22:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogUserFriendsVo {

    private Long userId;

    private Long friendId;


}
