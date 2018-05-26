package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * 小程序类型的属性，appid和title字段要么同时为空表示清除改属性，要么同时不为空
 */
class ExternalAttr_miniprogram_node {
    private final JsonNode node;

    ExternalAttr_miniprogram_node(JsonNode node) {
        this.node = node;
    }

    /**
     * 小程序appid，必须是有在本企业安装授权的小程序，否则会被忽略
     *
     * @return
     */
    public String appid() {
        return Optional.ofNullable(node)
                .map(it -> it.get("appid"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * 小程序的展示标题,长度限制12个UTF8字符
     *
     * @return
     */
    public String title() {
        return Optional.ofNullable(node)
                .map(it -> it.get("title"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * 小程序的页面路径
     *
     * @return
     */
    public String pagepath() {
        return Optional.ofNullable(node)
                .map(it -> it.get("pagepath"))
                .map(JsonNode::asText)
                .orElse(null);
    }
}
