package com.lrh.blog.search.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.search.pojo
 * @ClassName: Product
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/19 20:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(indexName = "article")
public class ArticleSearch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field(type = FieldType.Text , analyzer = "ik_max_word")
    private String articleTitle;

    @Field(type = FieldType.Text , analyzer = "ik_max_word")
    private String articleContent;


}
