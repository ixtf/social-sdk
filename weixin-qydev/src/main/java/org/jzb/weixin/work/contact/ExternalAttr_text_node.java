package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * 描述：
 *
 * @author jzb 2018-04-29
 */
class ExternalAttr_text_node {
    private final JsonNode node;

    ExternalAttr_text_node(JsonNode node) {
        this.node = node;
    }

    /**
     * 文本属性内容,长度限制12个UTF8字符
     *
     * @return
     */
    public String value() {
        return Optional.ofNullable(node)
                .map(it -> it.get("value"))
                .map(JsonNode::asText)
                .orElse(null);
    }
}
