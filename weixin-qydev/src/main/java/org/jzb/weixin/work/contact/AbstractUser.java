package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

/**
 * 描述：
 *
 * @author jzb 2018-04-29
 */
public abstract class AbstractUser {
    protected final JsonNode node;

    protected AbstractUser(JsonNode node) {
        this.node = node;
    }

    public String userid() {
        return Optional.ofNullable(node)
                .map(it -> it.get("userid"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public static User user(JsonNode node) {
        return new User(node);
    }

    public static UserSimpleListResponse_User userSimple(JsonNode node) {
        return new UserSimpleListResponse_User(node);
    }

    public static TagGetResponse_User userByTagGet(JsonNode node) {
        return new TagGetResponse_User(node);
    }

    public static AgentGetResponse_User userByAgentGet(JsonNode node) {
        return new AgentGetResponse_User(node);
    }
}
