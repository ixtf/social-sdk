package org.jzb.weixin.mp.msg_kf;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jzb.weixin.mp.MpClient;

import java.util.concurrent.Callable;

import static org.jzb.Constant.MAPPER;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class MpMsgKfRequestText extends MpMsgKfRequest {

    protected MpMsgKfRequestText(MpClient client, ObjectNode node, ObjectNode textNode) {
        super(client, node);
        node.put("msgtype", "text");
        node.set("text", textNode);
    }

    public static class Builder extends MpMsgKfRequest.Builder implements Callable<MpMsgKfResponse> {
        protected final ObjectNode textNode = MAPPER.createObjectNode();

        protected Builder(MpClient client, ObjectNode node) {
            super(client, node);
        }

        public Builder content(String content) {
            textNode.put("content", content);
            return this;
        }

        public MpMsgKfRequestText build() {
            return new MpMsgKfRequestText(client, node, textNode);
        }

        @Override
        public MpMsgKfResponse call() throws Exception {
            return this.build().call();
        }
    }
}
