package com.lrh.blog.common.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.domin
 * @ClassName: Reply
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/21 19:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long total;

    private List<BlogComments2> list;

    public Reply(List<BlogComments2> list) {
        this.list = list;
        this.total = (long) list.size();
    }
}
