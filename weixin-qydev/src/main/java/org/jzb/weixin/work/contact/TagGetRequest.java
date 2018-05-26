package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
import static org.jzb.weixin.work.util.WorkConstant.TAG_GET_URL_TPL;

/**
 * @author jzb 2017-10-28
 */
public class TagGetRequest implements Callable<TagGetResponse> {
    protected final AgentClient client;
    protected final long tagid;

    protected TagGetRequest(AgentClient client, long tagid) {
        this.client = client;
        this.tagid = tagid;
    }

    @Override
    public TagGetResponse call() throws Exception {
        final ImmutableMap<String, String> map = ImmutableMap.of(
                "access_token", client.access_token(),
                "tagid", "" + tagid
        );
        final Request request = new Request.Builder()
                .url(J.strTpl(TAG_GET_URL_TPL, map))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new TagGetResponse(client, node);
            }
        }
    }

    public static class Builder implements Callable<TagGetResponse> {
        protected final AgentClient client;
        protected long tagid;

        public Builder(AgentClient client) {
            this.client = client;
        }

        public Builder tagid(long tagid) {
            this.tagid = tagid;
            return this;
        }

        public TagGetRequest build() {
            return new TagGetRequest(client, tagid);
        }

        @Override
        public TagGetResponse call() throws Exception {
            return this.build().call();
        }
    }
}
