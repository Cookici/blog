package com.lrh.blog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.article.vo
 * @ClassName: BlogArticlesVo
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/6 21:54
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticlesVo {

    private String title;

    private String content;

}
