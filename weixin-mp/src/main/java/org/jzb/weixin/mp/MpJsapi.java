package org.jzb.weixin.mp;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.RandomStringUtils;
import org.jzb.J;
import org.jzb.social.core.AbstractExpireToken;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.jzb.Constant.MAPPER;
import static org.jzb.social.core.Constant.OK_CLIENT;
import static org.jzb.weixin.mp.util.MpConstant.JSAPI_TICKET_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-10-25
 */
class MpJsapi extends AbstractExpireToken<String> {
    private final MpClient client;
    private int expires_in;

    MpJsapi(MpClient client) {
        this.client = client;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    protected void fetch() throws Exception {
        final ImmutableMap<String, String> map = ImmutableMap.of("type", "jsapi", "access_token", client.access_token());
        final Request request = new Request.Builder()
                .url(J.strTpl(JSAPI_TICKET_URL_TPL, map))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                final JsonNode node = MAPPER.readTree(is);
                final Integer errcode = Optional.ofNullable(node.get("errcode"))
                        .map(JsonNode::asInt)
                        .orElse(0);
                if (errcode > 0) {
                    throw new Exception(node.toString());
                }
                final String ticket = node.get("ticket").asText();
                setT(ticket);
                final long expiresInMillis = getCreateInMillis() + TimeUnit.SECONDS.toMillis(expires_in - 30 * 60);
                setExpiresInMillis(expiresInMillis);
                expires_in = node.get("expires_in").asInt();
            }
        }
    }

    public Map<String, String> config(String url) throws Exception {
        Map<String, String> map = Maps.newHashMap();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = RandomStringUtils.randomAlphanumeric(6);

        String string1 = "jsapi_ticket=" + get() + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(string1.getBytes(StandardCharsets.UTF_8));
        String signature = byteToHex(crypt.digest());

        map.put("appId", client.getAppid());
        map.put("timestamp", timestamp);
        map.put("nonceStr", nonceStr);
        map.put("signature", signature);
        return map;
    }
}
