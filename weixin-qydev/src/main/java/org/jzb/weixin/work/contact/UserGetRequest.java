package org.jzb.weixin.work.contact;

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
import static org.jzb.weixin.work.util.WorkConstant.USER_GET_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class UserGetRequest implements Callable<UserGetResponse> {
    protected final AgentClient client;
    protected final long userid;

    protected UserGetRequest(AgentClient client, long userid) {
        this.client = client;
        this.userid = userid;
    }

    @Override
    public UserGetResponse call() throws Exception {
        final ImmutableMap<String, String> map = ImmutableMap.of(
                "access_token", client.access_token(),
                "userid", "" + userid
        );
        final Request request = new Request.Builder()
                .url(J.strTpl(USER_GET_URL_TPL, map))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new UserGetResponse(client, node);
            }
        }
    }

    public static class Builder implements Callable<UserGetResponse> {
        protected final AgentClient client;
        protected long userid;

        public Builder(AgentClient client) {
            this.client = client;
        }

        public Builder userid(long userid) {
            this.userid = userid;
            return this;
        }

        public UserGetRequest build() {
            return new UserGetRequest(client, userid);
        }

        @Override
        public UserGetResponse call() throws Exception {
            return this.build().call();
        }
    }
}
