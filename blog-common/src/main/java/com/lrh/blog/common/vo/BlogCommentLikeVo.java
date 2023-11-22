package com.lrh.blog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.vo
 * @ClassName: BlogCommentLikeVo
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/22 12:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogCommentLikeVo {

    private Long commentId;

    private Long userId;

}
