package org.jzb.social.core;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class Constant {
    public static final OkHttpClient OK_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.HOURS)
            .writeTimeout(1, TimeUnit.HOURS)
            .readTimeout(1, TimeUnit.HOURS)
            .build();
    public static final MediaType OK_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType OK_XML = MediaType.parse("application/xml; charset=utf-8");
    public static final XmlMapper XML_MAPPER = new XmlMapper();
}
