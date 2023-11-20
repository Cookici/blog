package com.lrh.blog.common.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.domin
 * @ClassName: ArticleSearch2
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/20 13:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleSearch2 implements Serializable {


    private static final long serialVersionUID = 1L;


    private Long id;


    private String articleTitle;


    private String articleContent;


}
