package com.lrh.blog.common.domin;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.domain
 * @ClassName: Message
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 21:51
 */

public class Message{
    /**
     * 发送方Id
     */
    private String fromId;

    /**
     * 接收方id
     */
    private String toId;

    /**
     * 消息内容
     */
    private String infoContent;

    /**
     * 消息类型 在线或离线
     */
    private String status;

    /**
     * 消息状态 在线或离线 single为私聊 group为群聊 add为添加好友
     */
    private String type;

    /**
     * 发送时间
     */
    private String time;


    /**
     * 接收到群聊消息的usersId
     */
    private List<String> userIdList;

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "fromId='" + fromId + '\'' +
                ", toId='" + toId + '\'' +
                ", infoContent='" + infoContent + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", userIdList=" + userIdList +
                '}';
    }
}
