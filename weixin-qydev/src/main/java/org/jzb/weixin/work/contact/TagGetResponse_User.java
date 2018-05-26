package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * 描述：
 *
 * @author jzb 2018-04-29
 */
public class TagGetResponse_User extends AbstractUser {
    TagGetResponse_User(JsonNode node) {
        super(node);
    }

    public String name() {
        return Optional.ofNullable(node)
                .map(it -> it.get("name"))
                .map(JsonNode::asText)
                .orElse(null);
    }
}
