package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * 描述：
 *
 * @author jzb 2018-04-29
 */
abstract class AbstractExternalAttr {
    protected final JsonNode node;

    protected AbstractExternalAttr(JsonNode node) {
        this.node = node;
    }

    public static AbstractExternalAttr instant(JsonNode node) {
        return Optional.ofNullable(node)
                .map(it -> it.get("type"))
                .map(JsonNode::asInt)
                .map(it -> {
                    switch (it) {
                        case 0:
                            return new ExternalAttr_text(node);
                        case 1:
                            return new ExternalAttr_web(node);
                        case 2:
                            return new ExternalAttr_miniprogram(node);
                    }
                    throw new RuntimeException("type[" + it + "]：没有对应的类型");
                })
                .orElse(null);
    }


    /**
     * 属性类型: 0-本文 1-网页 2-小程序
     *
     * @return
     */
    public String type() {
        return Optional.ofNullable(node)
                .map(it -> it.get("type"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * 属性名称： 需要先确保在管理端有创建改属性，否则会忽略
     *
     * @return
     */
    public String name() {
        return Optional.ofNullable(node)
                .map(it -> it.get("name"))
                .map(JsonNode::asText)
                .orElse(null);
    }
}
