package com.lrh.blog.search.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.search.config
 * @ClassName: ElasticSearchConfig
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/19 19:44
 */
@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.host}")
    public String host;

    @Value("${elasticsearch.port}")
    public Integer port;

    @Bean
    RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http"))
                        .setRequestConfigCallback(builder -> builder.setConnectTimeout(5000 * 1000)
                                .setSocketTimeout(6000 * 1000))
        );

    }

}
