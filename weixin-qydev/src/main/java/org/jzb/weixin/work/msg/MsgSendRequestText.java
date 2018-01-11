package org.jzb.weixin.work.msg;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import org.jzb.J;
import org.jzb.weixin.work.AgentClient;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static org.jzb.Constant.MAPPER;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class MsgSendRequestText extends MsgSendRequest {

    protected MsgSendRequestText(AgentClient client, ObjectNode node, ObjectNode textNode) {
        super(client, node);
        node.put("agentid", client.getAgentid());
        node.put("msgtype", "text");
        node.set("text", textNode);
    }

    public static class Builder extends MsgSendRequest.Builder implements Callable<MsgSendResponse> {
        protected final ObjectNode node = MAPPER.createObjectNode();
        protected final ObjectNode textNode = MAPPER.createObjectNode();
        private final Set<String> touserSet = Sets.newHashSet();
        private final Set<String> topartySet = Sets.newHashSet();
        private final Set<String> totagSet = Sets.newHashSet();
        private boolean all = false;

        protected Builder(AgentClient client) {
            super(client);
        }

        public Builder content(String content) {
            textNode.put("content", content);
            return this;
        }

        public Builder addUser(String s) {
            touserSet.add(s);
            return this;
        }

        /**
         * 表示是否是保密消息，
         *
         * @param s 0表示否，1表示是，默认0
         */
        public void safe(String s) {
            node.put("safe", s);
        }

        public MsgSendRequestText build() {
            if (all) {
                node.put("touser", "@all");
                return new MsgSendRequestText(client, node, textNode);
            }
            if (J.nonEmpty(touserSet)) {
                final String touser = touserSet.stream()
                        .collect(Collectors.joining("|"));
                node.put("touser", touser);
            }
            if (J.nonEmpty(topartySet)) {
                final String toparty = topartySet.stream()
                        .collect(Collectors.joining("|"));
                node.put("toparty", "toparty");
            }
            if (J.nonEmpty(totagSet)) {
                final String totag = totagSet.stream()
                        .collect(Collectors.joining("|"));
                node.put("toparty", totag);
            }
            return new MsgSendRequestText(client, node, textNode);
        }

        @Override
        public MsgSendResponse call() throws Exception {
            return this.build().call();
        }
    }
}
