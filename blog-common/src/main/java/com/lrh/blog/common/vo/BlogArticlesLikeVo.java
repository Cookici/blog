package com.lrh.blog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.vo
 * @ClassName: BlogArticlesLikeVo
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/22 11:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticlesLikeVo {

    private Long articleId;

    private Long userId;

}
