package com.lrh.blog.identify.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.config
 * @ClassName: KaptchaConfig
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/22 12:34
 */

@Configuration
public class KaptchaConfig {

    /**
     * DefaultKaptcha实现了Producer接口，Producer接口用于生成验证码，调用其createText()方法即可生成字符串验证码
     * @return
     */

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.textproducer.char.space", "4");
        properties.put("kaptcha.image.height", "40");
        properties.put("kaptcha.image.width", "120");
        properties.put("kaptcha.textproducer.font.size", "30");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}
