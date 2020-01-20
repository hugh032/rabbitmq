package com.example.demo.entity;

/**
 * 消息实体
 * @Author zhaojun
 * @create 2020/1/20 14:23
 */
public class Message{
    private String messageId;
    private String messageContent;
    private String date;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
