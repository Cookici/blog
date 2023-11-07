package com.lrh.blog.chat.pojo.packet;

import com.lrh.blog.chat.domain.User;
import com.lrh.blog.chat.pojo.AbstractPacket;
import com.lrh.blog.chat.pojo.Type;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.pojo.packet
 * @ClassName: RegisterPacket
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 18:06
 */

public class RegisterPacket extends AbstractPacket {


    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Byte getCommand() {
        return Type.REGISTER;
    }


}
