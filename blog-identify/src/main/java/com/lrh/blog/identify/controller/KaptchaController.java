package com.lrh.blog.identify.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.identify.utils.RedisUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.controller
 * @ClassName: KaptcherController
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/24 15:37
 */
@RestController
public class KaptchaController {


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private Producer producer;

    @GetMapping("/captcha")
    public Result<Map<Object, Object>> captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(outputStream.toByteArray());

        redisUtils.hset("captcha", key, code,120);


        return Result.ok(MapUtil.builder()
                .put("userKey", key)
                .put("captcherImg", base64Img)
                .build());
    }

}
