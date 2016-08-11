package com.chenh.smartclassroom.vo;

import java.util.Date;

/**
 * Created by chenh on 2016/8/1.
 */
public class BlogComments {

    /**
     *
     */
    public long id;

    public long rawMessageId;

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
}
