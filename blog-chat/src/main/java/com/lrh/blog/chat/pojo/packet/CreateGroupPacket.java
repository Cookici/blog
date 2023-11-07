package com.lrh.blog.chat.pojo.packet;

import com.lrh.blog.chat.pojo.AbstractPacket;
import com.lrh.blog.chat.pojo.Type;

import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.pojo.packet
 * @ClassName: CreateGroupPacket
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 18:15
 */

public class CreateGroupPacket extends AbstractPacket {
    /**
     * 创建群时需要把群里的Id发过来
     */
    private Integer groupId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    private List<String> userIdList;

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    @Override
    public Byte getCommand() {
        return Type.CREATE_GROUP;
    }
}
