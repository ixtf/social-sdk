package org.jzb.weixin.work.agent;

import com.fasterxml.jackson.databind.JsonNode;
import org.jzb.weixin.work.AgentClient;
import org.jzb.weixin.work.contact.AbstractUser;
import org.jzb.weixin.work.contact.AgentGetResponse_User;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class AgentGetResponse {
    protected final AgentClient client;
    protected final JsonNode node;

    public AgentGetResponse(AgentClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    public Stream<AgentGetResponse_User> allow_userinfos() {
        return Optional.ofNullable(node)
                .map(it -> it.get("allow_userinfos"))
                .map(it -> it.get("user"))
                .map(it -> StreamSupport.stream(it.spliterator(), false)
                        .map(AbstractUser::userByAgentGet)
                )
                .orElse(Stream.empty());
    }

    public Stream<Long> allow_partys() {
        return Optional.ofNullable(node)
                .map(it -> it.get("allow_partys"))
                .map(it -> it.get("partyid"))
                .map(it -> StreamSupport.stream(it.spliterator(), false)
                        .map(JsonNode::asLong)
                )
                .orElse(Stream.empty());
    }

    public Stream<Long> allow_tags() {
        return Optional.ofNullable(node)
                .map(it -> it.get("allow_tags"))
                .map(it -> it.get("tagid"))
                .map(it -> StreamSupport.stream(it.spliterator(), false)
                        .map(JsonNode::asLong)
                )
                .orElse(Stream.empty());
    }

    public boolean isSuccessed() {
        return errcode() == 0;
    }

    public int errcode() {
        return node.get("errcode").asInt();
    }

    public String errmsg() {
        return node.get("errcode").asText();
    }

    public long msgid() {
        return node.get("errcode").asLong();
    }
}

