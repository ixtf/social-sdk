package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 描述：成员对外属性，暂不支持第三方使用，字段详情见对外属性；第三方暂不可获取
 *
 * @author jzb 2018-04-29
 * @see <a href="https://work.weixin.qq.com/api/doc#13450">对外属性</a>
 */
public class ExternalProfile {
    private final JsonNode node;

    ExternalProfile(JsonNode node) {
        this.node = node;
    }

    public Stream<? extends AbstractExternalAttr> department() {
        return Optional.ofNullable(node)
                .map(it -> it.get("external_attr"))
                .map(it -> StreamSupport.stream(it.spliterator(), false)
                        .map(AbstractExternalAttr::instant)
                )
                .orElse(Stream.empty());
    }
}
