package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * 描述：
 *
 * @author jzb 2018-04-29
 */
class ExternalAttr_text extends AbstractExternalAttr {
    ExternalAttr_text(JsonNode node) {
        super(node);
    }

    public ExternalAttr_text_node text() {
        return Optional.ofNullable(node)
                .map(it -> it.get("text"))
                .map(ExternalAttr_text_node::new)
                .orElse(null);
    }
}
