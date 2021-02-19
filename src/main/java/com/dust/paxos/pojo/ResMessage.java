package com.dust.paxos.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * 存储端给客户端的响应信息
 */
@Getter
@Setter
public class ResMessage {

    /**
     * 是Acceptor记住的最后一次进行写前读取的Proposer是谁，以此来决定谁可以在后面真正把一个值写入到存储中。
     */
    private int lastRnd;

    /**
     * 跟v一对，记录了在哪个Round中v被写入了
     */
    private int vrnd;

    /**
     * 最后被写入的值
     */
    private String value;

    /**
     * 是否成功
     */
    private boolean success;

}
