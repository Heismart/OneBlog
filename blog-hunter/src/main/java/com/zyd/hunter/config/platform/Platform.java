package com.zyd.hunter.config.platform;

import com.zyd.hunter.util.PlatformUtil;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
public enum Platform {
    CSDN("csdn", "csdn.net", CsdnPlatform.class),
    ITEYE("iteye", "iteye.com", IteyePlatform.class),
    IMOOC("imooc", "imooc.com", ImoocPlatform.class),
    CNBLOGS("cnblogs", "cnblogs.com", CnblogsPlatform.class),
    JUEJIN("juejin", "juejin.im", JuejinPlatform.class),
    V2EX("v2ex", "v2ex.com", V2exPlatform.class),
    OSCHINA("oschina", "oschina.net", OschinaPlatform.class),
    JIANSHU("jianshu","jianshu.com",JianshuPlatform.class)
    ;

    private final String platform;
    private final String host;
    private final Class clazz;

    Platform(String platform, String host, Class clazz) {
        this.platform = platform;
        this.host = host;
        this.clazz = clazz;
    }

    public static Platform getPlatformByUrl(String url) {
        if (null == url) {
            return null;
        }
        String host = PlatformUtil.getHost(url);
        if (host == null) {
            return null;
        }
        for (Platform value : Platform.values()) {
            if (host.contains(value.getHost())) {
                return value;
            }
        }
        return null;
    }

    public String getPlatform() {
        return platform;
    }

    public String getHost() {
        return host;
    }

    public Class getClazz() {
        return clazz;
    }

}
