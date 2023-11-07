package com.lrh.blog.chat.pojo.packet;

import com.lrh.blog.chat.pojo.AbstractPacket;
import com.lrh.blog.chat.pojo.Type;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.pojo.packet
 * @ClassName: SingleMessagePacket
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 18:12
 */

public class SingleMessagePacket extends AbstractPacket {

    /**
     * 要发送给目标用户的Id
     */
    private String toUserId;

    private String message;

    private String fileType;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public Byte getCommand() {
        return Type.SINGLE_MESSAGE;
    }
}
