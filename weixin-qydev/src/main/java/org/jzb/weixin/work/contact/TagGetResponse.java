package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;
import org.jzb.weixin.work.AgentClient;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class TagGetResponse {
    protected final AgentClient client;
    protected final JsonNode node;

    public TagGetResponse(AgentClient client, JsonNode node) {
        this.client = client;
        this.node = node;
    }

    public Stream<TagGetResponse_User> userlist() {
        return Optional.ofNullable(node)
                .map(it -> it.get("userlist"))
                .map(it -> StreamSupport.stream(it.spliterator(), true)
                        .map(AbstractUser::userByTagGet)
                )
                .orElse(Stream.empty());
    }

    public Stream<Long> partylist() {
        return Optional.ofNullable(node)
                .map(it -> it.get("partylist"))
                .map(it -> StreamSupport.stream(it.spliterator(), true)
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
