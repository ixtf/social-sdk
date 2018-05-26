package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * 描述：
 *
 * @author jzb 2018-04-29
 */
class ExternalAttr_miniprogram extends AbstractExternalAttr {
    ExternalAttr_miniprogram(JsonNode node) {
        super(node);
    }

    public ExternalAttr_miniprogram_node miniprogram() {
        return Optional.ofNullable(node)
                .map(it -> it.get("miniprogram"))
                .map(ExternalAttr_miniprogram_node::new)
                .orElse(null);
    }

}
