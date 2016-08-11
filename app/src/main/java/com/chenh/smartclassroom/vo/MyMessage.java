package com.chenh.smartclassroom.vo;

import java.util.Date;
import java.util.UUID;

/**
 * Created by chenh on 2016/7/27.
 */
public abstract class MyMessage {

    /**
     *
     */
    public UUID uuid;

    /**
     * 消息标题
     */
    public String title;

    /**
     * 发送时间
     */
    public Date sendTime;

    /**
     * 消息发出者
     */
    public User author;

    /**
     * 消息正文
     */
    public String text;

    /**
     *消息接收者
     */
    public User receiver;

}
