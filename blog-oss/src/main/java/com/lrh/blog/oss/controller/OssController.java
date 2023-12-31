package com.lrh.blog.oss.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.vo.BlogPhotoVo;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.oss
 * @ClassName: OssController
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/23 18:45
 */

@RestController
@RefreshScope
@RequestMapping("/blog/oss")
public class OssController {

    @Autowired
    private OSS ossClient;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String secretKey;

    @PostMapping("/policy/{username}")
    public Result<Map<String, String>> policy(HttpServletResponse response, @PathVariable("username") String username) {
        // 填写Host地址，格式为https://bucketname.endpoint。
        String host = "https://" + bucket;
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下
        String dir = username + "/";
        // 创建ossClient实例。
        ossClient = new OSSClientBuilder().build(endpoint, accessId, secretKey);
        Map<String, String> respMap = null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Result.ok(respMap);
    }


    @DeleteMapping("/deleteFile")
    public Result<Object> deleteFileByFileUrl(@RequestBody BlogPhotoVo blogPhotoVo) {
        try {
            String userName = blogPhotoVo.getUserName();
            String url = blogPhotoVo.getPhotoUrl();
            int index = url.indexOf(userName);
            if (index != -1) {
                String fileName = url.substring(index);
                System.out.println(fileName);
                ossClient = new OSSClientBuilder().build(endpoint, accessId, secretKey);
                ossClient.deleteObject("lrh-blog-project", fileName);
                return Result.ok().message("删除成功");
            }
        } catch (Exception e) {
            return Result.fail().message("未知错误");
        }
        return Result.fail().message("非法操作");

    }

}
