package com.lrh.blog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.vo
 * @ClassName: BlogGroupVo
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/14 20:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogGroupVo {

    private Long groupId;
    private String groupName;
    private List<Long> ids;

}
