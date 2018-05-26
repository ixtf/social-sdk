package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author jzb 2018-04-29
 */
public class UserSimpleListResponse_User extends AbstractUser {
    UserSimpleListResponse_User(JsonNode node) {
        super(node);
    }

    public String name() {
        return Optional.ofNullable(node)
                .map(it -> it.get("name"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public Stream<Long> department() {
        return Optional.ofNullable(node)
                .map(it -> it.get("department"))
                .map(it -> StreamSupport.stream(it.spliterator(), false)
                        .map(JsonNode::asLong)
                )
                .orElse(Stream.empty());
    }

}
