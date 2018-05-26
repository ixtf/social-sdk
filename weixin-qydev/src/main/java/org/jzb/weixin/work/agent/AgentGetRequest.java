package org.jzb.weixin.work.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jzb.J;
import org.jzb.weixin.work.AgentClient;

import java.io.InputStream;
import java.util.concurrent.Callable;

import static org.jzb.Constant.MAPPER;
import static org.jzb.social.core.Constant.OK_CLIENT;
import static org.jzb.weixin.work.util.WorkConstant.AGENT_GET_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class AgentGetRequest implements Callable<AgentGetResponse> {
    protected final AgentClient client;
    protected final int agentid;

    protected AgentGetRequest(AgentClient client, int agentid) {
        this.client = client;
        this.agentid = agentid;
    }

    @Override
    public AgentGetResponse call() throws Exception {
        final ImmutableMap<String, String> map = ImmutableMap.of(
                "access_token", client.access_token(),
                "agentid", "" + agentid
        );
        final Request request = new Request.Builder()
                .url(J.strTpl(AGENT_GET_URL_TPL, map))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new AgentGetResponse(client, node);
            }
        }
    }

    public static class Builder implements Callable<AgentGetResponse> {
        protected final AgentClient client;
        protected final int agentid;

        public Builder(AgentClient client, int agentid) {
            this.client = client;
            this.agentid = agentid;
        }

        public AgentGetRequest build() {
            return new AgentGetRequest(client, agentid);
        }

        @Override
        public AgentGetResponse call() throws Exception {
            return this.build().call();
        }
    }
}
