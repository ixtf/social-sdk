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
import static org.jzb.weixin.work.util.WorkConstant.USER_LIST_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class UserListRequest implements Callable<UserListResponse> {
    protected final AgentClient client;
    protected final long department_id;
    protected final boolean fetch_child;

    protected UserListRequest(AgentClient client, long department_id, boolean fetch_child) {
        this.client = client;
        this.department_id = department_id;
        this.fetch_child = fetch_child;
    }

    @Override
    public UserListResponse call() throws Exception {
        final ImmutableMap<String, String> map = ImmutableMap.of(
                "access_token", client.access_token(),
                "department_id", "" + department_id,
                "fetch_child", fetch_child ? "1" : "0"
        );
        final Request request = new Request.Builder()
                .url(J.strTpl(USER_LIST_URL_TPL, map))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new UserListResponse(client, node);
            }
        }
    }

    public static class Builder implements Callable<UserListResponse> {
        protected final AgentClient client;
        protected long department_id;
        protected boolean fetch_child;

        public Builder(AgentClient client) {
            this.client = client;
        }

        public Builder department_id(long department_id) {
            this.department_id = department_id;
            return this;
        }

        public Builder fetch_child(boolean fetch_child) {
            this.fetch_child = fetch_child;
            return this;
        }

        public UserListRequest build() {
            return new UserListRequest(client, department_id, fetch_child);
        }

        @Override
        public UserListResponse call() throws Exception {
            return this.build().call();
        }
    }
}
