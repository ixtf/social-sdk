package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * 描述：
 *
 * @author jzb 2018-04-29
 */
class ExternalAttr_web extends AbstractExternalAttr {
    ExternalAttr_web(JsonNode node) {
        super(node);
    }

    public ExternalAttr_web_node web() {
        return Optional.ofNullable(node)
                .map(it -> it.get("web"))
                .map(ExternalAttr_web_node::new)
                .orElse(null);
    }

}
