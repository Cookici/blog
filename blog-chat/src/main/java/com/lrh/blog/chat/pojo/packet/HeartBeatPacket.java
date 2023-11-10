package com.lrh.blog.chat.pojo.packet;

import com.lrh.blog.chat.pojo.AbstractPacket;
import com.lrh.blog.chat.pojo.Type;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.pojo.packet
 * @ClassName: HeartBeatPacket
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/10 8:09
 */

public class HeartBeatPacket extends AbstractPacket {

    private String userId;

    private String message;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public Byte getCommand() {
        return Type.HEART_BEAT;
    }

    @Override
    public String toString() {
        return "HeartBeatPacket{" +
                "userId='" + userId + '\'' +
                ", message='" + message +
                "} " + super.toString();
    }
}
