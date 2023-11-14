package com.lrh.blog.common.vo;

import com.lrh.blog.common.entity.BlogGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.vo
 * @ClassName: GroupPhotosVo
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/14 22:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPhotosVo {

    private BlogGroup blogGroup;

    private List<String> photosUrl;

}
