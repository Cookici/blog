package com.lrh.blog.common.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.domin
 * @ClassName: BlogUsers2
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/21 19:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogUsers2 implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String avatar;

    private Integer level;

    private String homeLink;

}
