package com.zyd.hunter.config;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyd.hunter.consts.HunterConsts;
import com.zyd.hunter.exception.HunterException;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
public class HunterConfigTemplate {

    public static JSONObject configTemplate;

    static {
        HunterConfigTemplate configTemplate = new HunterConfigTemplate();
        configTemplate.init();
    }

    public static String getConfig(String platform) {
        if (configTemplate.containsKey(platform)) {
            return configTemplate.getString(platform);
        }
        throw new HunterException("暂不支持该平台[" + platform + "]");
    }

    private void init() {
        String configFileName = HunterConsts.CONFIG_FILE_NAME;
        String config = null;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(configFileName);
            if (null == inputStream) {
                throw new HunterException("请检查`src/main/resources`下是否存在" + configFileName);
            }
            config = IoUtil.read(inputStream, StandardCharsets.UTF_8);
            if (StringUtils.isEmpty(config)) {
                throw new HunterException("HunterConfig内容为空：" + configFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            configTemplate = JSON.parseObject(config);
        } catch (Exception e) {
            throw new HunterException("HunterConfig配置文件格式错误");
        }

    }

}
