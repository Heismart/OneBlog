package com.zyd.blog.framework.redis;

import org.crazycake.shiro.RedisManager;
/**
 * 自定义org.crazycake.shiro.RedisManager。<br/>
 * 该自定义的Manager扩展的功能：<br/>
 * 1.修改expire参数，默认值为7天 = 604800 sec <br/>
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://docs.zhyd.me
 * @date 2018/6/12 14:22
 *       2023/10/7 14:02  废弃无必要的自定义
 * @since 1.0
 *
 */
public class CustomRedisManager extends RedisManager {
    public CustomRedisManager(){
        this.setExpire(604800);
    }
}
