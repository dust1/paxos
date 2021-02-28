package com.dust.paxos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRes {

    private String key;
    private String value;
    private boolean success;


    public static GetRes byMetaData(MetaData metaData) {
        return new GetRes(metaData.getKey(), metaData.getValue(), true);
    }

    public static GetRes empty(String key) {
        return new GetRes(key, null,false);
    }

    private GetRes(String key, String value, boolean success) {
        this.key = key;
        this.value = value;
        this.success = success;
    }

}
