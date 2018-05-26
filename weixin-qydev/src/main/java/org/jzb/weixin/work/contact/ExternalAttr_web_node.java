package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * 描述：
 *
 * @author jzb 2018-04-29
 */
class ExternalAttr_web_node {
    private final JsonNode node;

    ExternalAttr_web_node(JsonNode node) {
        this.node = node;
    }

    /**
     * 网页的url,必须包含http或者https头
     *
     * @return
     */
    public String url() {
        return Optional.ofNullable(node)
                .map(it -> it.get("url"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * 网页的展示标题,长度限制12个UTF8字符
     *
     * @return
     */
    public String title() {
        return Optional.ofNullable(node)
                .map(it -> it.get("title"))
                .map(JsonNode::asText)
                .orElse(null);
    }
}
