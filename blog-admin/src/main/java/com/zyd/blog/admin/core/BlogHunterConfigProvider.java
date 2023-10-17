package com.zyd.blog.admin.core;

import com.alibaba.fastjson.JSON;
import com.zyd.blog.business.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import com.zyd.hunter.config.HunterConfigTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Component
public class BlogHunterConfigProvider {

    @Autowired
    private SysConfigService sysConfigService;

    public String getBlogHunterConfig() {
        String config = sysConfigService.getSpiderConfig();
        if (StringUtils.isEmpty(config) || "{}".equals(config)) {
            return HunterConfigTemplate.configTemplate.toJSONString();
        }
        try {
            return JSON.parseObject(config).toJSONString();
        } catch (Exception e) {
            log.error("blog-hunter 的配置文件格式错误！将启用默认的 `src/main/resources/HunterConfig.json` 内容！", e);
        }
        return "{}";
    }

}
