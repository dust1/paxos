package com.dust.paxos.pojo;

import lombok.Data;

/**
 * 客户端往存储端发送的请求
 */
@Data
public class ReqMessage {
    
    private Integer rnd;

    private String v;
    
}
