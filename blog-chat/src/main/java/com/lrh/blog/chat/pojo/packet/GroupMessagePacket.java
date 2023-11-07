package com.lrh.blog.chat.pojo.packet;

import com.lrh.blog.chat.pojo.AbstractPacket;
import com.lrh.blog.chat.pojo.Type;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.pojo.packet
 * @ClassName: GroupMessagePacket
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 18:18
 */

public class GroupMessagePacket extends AbstractPacket {

    /**
     *     变成群之后就是一个人对一个群发的消息 没有@ 的功能
     *     toGroupId为要送给的群聊ID
     */


    private Integer toGroupId;

    private String message;

    private String fileType;

    public Integer getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(Integer toGroupId) {
        this.toGroupId = toGroupId;
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
        return Type.GROUP_MESSAGE;
    }

}
