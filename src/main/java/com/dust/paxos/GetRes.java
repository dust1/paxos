package com.dust.paxos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRes {

    private String key;
    private String value;
    private boolean success;
    private int lastRnd;
    private String acceptId;


    public static GetRes byMetaData(MetaData metaData, String acceptId) {
        return new GetRes(metaData.getKey(), metaData.getValue(), true, metaData.getLastRnd(), acceptId);
    }

    public static GetRes empty(String key, String acceptId) {
        return new GetRes(key, null,false, 0, acceptId);
    }

    private GetRes(String key, String value, boolean success, int lastRnd, String acceptId) {
        this.key = key;
        this.value = value;
        this.success = success;
        this.acceptId = acceptId;
        this.lastRnd = lastRnd;
    }


}
