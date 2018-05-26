package org.jzb.weixin.work.contact;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 描述：
 *
 * @author jzb 2018-04-29
 */
public class User extends AbstractUser {
    User(JsonNode node) {
        super(node);
    }

    public String name() {
        return Optional.ofNullable(node)
                .map(it -> it.get("name"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public String position() {
        return Optional.ofNullable(node)
                .map(it -> it.get("position"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * 性别
     * 0表示未定义，1表示男性，2表示女性
     *
     * @return
     */
    public String gender() {
        return Optional.ofNullable(node)
                .map(it -> it.get("gender"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public String email() {
        return Optional.ofNullable(node)
                .map(it -> it.get("email"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * 上级字段，标识是否为上级；第三方仅通讯录套件可获取
     *
     * @return
     */
    public boolean isleader() {
        return Optional.ofNullable(node)
                .map(it -> it.get("isleader"))
                .map(JsonNode::asBoolean)
                .orElse(null);
    }

    public String avatar() {
        return Optional.ofNullable(node)
                .map(it -> it.get("avatar"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public String mobile() {
        return Optional.ofNullable(node)
                .map(it -> it.get("mobile"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public String telephone() {
        return Optional.ofNullable(node)
                .map(it -> it.get("telephone"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public String english_name() {
        return Optional.ofNullable(node)
                .map(it -> it.get("english_name"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * 激活状态
     * 1=已激活，2=已禁用，4=未激活
     * 已激活代表已激活企业微信或已关注微信插件
     * 未激活代表既未激活企业微信又未关注微信插件
     *
     * @return
     */
    public String status() {
        return Optional.ofNullable(node)
                .map(it -> it.get("status"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    /**
     * 员工个人二维码，扫描可添加为外部联系人；第三方暂不可获取
     *
     * @return
     */
    public String qr_code() {
        return Optional.ofNullable(node)
                .map(it -> it.get("qr_code"))
                .map(JsonNode::asText)
                .orElse(null);
    }

    public ExternalProfile external_profile() {
        return Optional.ofNullable(node)
                .map(it -> it.get("external_profile"))
                .map(ExternalProfile::new)
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

    /**
     * 部门内的排序值，默认为0。数量必须和department一致，数值越大排序越前面。值范围是[0, 2^32)
     *
     * @return
     */
    public Stream<Long> order() {
        return Optional.ofNullable(node)
                .map(it -> it.get("order"))
                .map(it -> StreamSupport.stream(it.spliterator(), false)
                        .map(JsonNode::asLong)
                )
                .orElse(Stream.empty());
    }
}
