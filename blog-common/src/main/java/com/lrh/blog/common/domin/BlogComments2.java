package com.lrh.blog.common.domin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.domin
 * @ClassName: BlogComments2
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/21 19:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogComments2 implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String parentId;

    private String uid;

    private String content;

    private String address;

    private Long likes;

    private String contentImg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private BlogUsers2 user;

    private Reply reply;

}
