package org.jzb.weixin.mp.unionInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jzb.J;
import org.jzb.weixin.mp.MpClient;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.jzb.Constant.MAPPER;
import static org.jzb.social.core.Constant.OK_CLIENT;
import static org.jzb.weixin.mp.util.MpConstant.UNION_INFO_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-28
 */
public class MpUnionInfoRequest implements Callable<MpUnionInfoResponse> {
    private final MpClient client;
    private final Map<String, String> map;

    private MpUnionInfoRequest(MpClient client, Map<String, String> map) {
        this.client = client;
        this.map = map;
    }

    @Override
    public MpUnionInfoResponse call() throws Exception {
        Map<String, String> map = Maps.newHashMap(this.map);
        map.put("access_token", client.access_token());
        final Request request = new Request.Builder()
                .url(J.strTpl(UNION_INFO_URL_TPL, map))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                return new MpUnionInfoResponse(client, node);
            }
        }
    }

    public static class Builder implements Callable<MpUnionInfoResponse> {
        private final MpClient client;
        private final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

        public Builder(MpClient client) {
            this.client = client;
            builder.put("lang", "zh_CN");
        }

        public Builder openid(String openid) {
            builder.put("openid", openid);
            return this;
        }

        public Builder lang(String lang) {
            builder.put("lang", lang);
            return this;
        }

        public MpUnionInfoRequest build() {
            return new MpUnionInfoRequest(client, builder.build());
        }

        @Override
        public MpUnionInfoResponse call() throws Exception {
            return this.build().call();
        }
    }
}
