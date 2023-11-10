package com.lrh.blog.chat.pojo.packet;

import com.lrh.blog.chat.pojo.AbstractPacket;
import com.lrh.blog.chat.pojo.Type;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.pojo.packet
 * @ClassName: AddFriendPacket
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/8 21:57
 */

public class AddFriendPacket extends AbstractPacket {


    private String fromUserId;

    private String toUserId;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }


    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    @Override
    public Byte getCommand() {
        return Type.ADD_FRIEND;
    }
}
