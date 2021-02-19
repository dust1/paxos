package com.dust.paxos.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * 节点抽象类
 */
@Getter
@Setter
public abstract class Node {
    
    protected String uuid;

    protected Node(String uuid) {
        this.uuid = uuid;
    }

}
