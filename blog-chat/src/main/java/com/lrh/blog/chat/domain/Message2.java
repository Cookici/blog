package com.lrh.blog.chat.domain;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.domain
 * @ClassName: Message2
 * @Author: 63283
 * @Description: 返回前端的消息主题与消息队列中储存的消息
 * @Date: 2023/11/7 20:29
 */

public class Message2 {

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
    private String type;

    /**
     * 消息状态 在线或离线 1为私聊 0为群聊
     */
    private Boolean status;

    /**
     * 发送时间
     */
    private String time;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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
}
