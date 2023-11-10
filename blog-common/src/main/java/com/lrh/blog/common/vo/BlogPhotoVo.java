package com.lrh.blog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.vo
 * @ClassName: PhotoVo
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/9 14:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPhotoVo {

    private String id;

    private String photoUrl;

    private String userName;

}
