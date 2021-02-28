package com.dust.paxos;

import lombok.Getter;
import lombok.Setter;

/**
 * 写前读取返回结果
 */
@Getter
@Setter
public class ReadRes {

    private int lastRnd;

    private String value;

    private int vrnd;

    private boolean success;

    public static ReadRes fail() {
        return new ReadRes(-1, null, -1, false);
    }

    public static ReadRes byMetaData(MetaData metaData) {
        return new ReadRes(metaData.getLastRnd(), metaData.getValue(), metaData.getVrnd(), true);
    }

    private ReadRes(int lastRnd, String value, int vrnd, boolean success) {
        this.lastRnd = lastRnd;
        this.value = value;
        this.vrnd = vrnd;
        this.success = success;
    }

}
