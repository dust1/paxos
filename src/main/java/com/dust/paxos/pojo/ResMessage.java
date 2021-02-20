package com.dust.paxos.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 存储端给客户端的响应信息
 */
@Getter
@Setter
@ToString
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

    public static ResMessage create(MetaData metaData) {
        return new ResMessage(metaData);
    }

    private ResMessage(MetaData metaData) {
        this.lastRnd = metaData.getLastRnd();
        this.vrnd = metaData.getVrnd();
        this.value = metaData.getValue();
    }

}
