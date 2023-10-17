package com.zyd.hunter.config.platform;

import com.zyd.hunter.config.HunterConfig;

/**
 * @author huht
 * @version 1.01
 * @since 1.8
 */
public class OschinaPlatform extends BasePlatform {

    public OschinaPlatform() {
        super(Platform.OSCHINA.getPlatform());
    }

    @Override
    public HunterConfig process(String url) {
        return this.get(url);
    }
}
