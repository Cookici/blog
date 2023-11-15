package com.lrh.blog.common.vo;

import com.lrh.blog.common.domin.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.vo
 * @ClassName: noReadGroupMessage
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/15 22:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoReadGroupMessage {

    private String groupId;

    private Long noReadMessageSize;


}
