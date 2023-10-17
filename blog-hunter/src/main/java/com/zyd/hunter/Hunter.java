package com.zyd.hunter;

import com.zyd.hunter.config.HunterConfig;
import com.zyd.hunter.enums.ExitWayEnum;
import com.zyd.hunter.exception.HunterException;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
public class Hunter extends Spider {

    /**
     * 用来保存正在运行的所有Spider，key要求唯一，一般为用户ID，需要调用方生成
     */
    private static final ConcurrentMap<String, Hunter> SPIDER_BUCKET = new ConcurrentHashMap<>();

    private final HunterConfig config;

    /**
     * 唯一的key，一般为用户ID，需要调用方生成
     */
    private final String hunterId;
    private volatile long startTime = 0L;

    private Hunter(PageProcessor pageProcessor, HunterConfig config, String hunterId) {
        super(pageProcessor);
        this.config = config;
        this.hunterId = hunterId;
        SPIDER_BUCKET.put(hunterId, this);
    }

    public static Hunter create(PageProcessor pageProcessor, HunterConfig config, String hunterId) {
        return new Hunter(pageProcessor, config, hunterId);
    }

    public static Hunter getHunter(String hunterId) {
        if (StringUtils.isEmpty(hunterId)) {
            throw new HunterException("HunterId：[" + hunterId + "]为空，请指定HunterId");
        }
        Hunter hunter = SPIDER_BUCKET.get(hunterId);
        if (null == hunter) {
            throw new HunterException("当前没有正在运行的爬虫！HunterId：[" + hunterId + "]");
        }
        return hunter;
    }

    @Override
    protected void onSuccess(Request request) {
        super.onSuccess(request);
        if (this.getStatus() == Status.Running && ExitWayEnum.DURATION.toString().equals(config.getExitWay())) {
            if (startTime < System.currentTimeMillis()) {
                this.stop();
            }
        }
    }

    @Override
    public void run() {
        if (ExitWayEnum.DURATION.toString().equals(config.getExitWay())) {
            startTime = System.currentTimeMillis() + config.getCount() * 1000L;
        }
        super.run();
    }

    @Override
    public void close() {
        super.close();
        SPIDER_BUCKET.remove(this.hunterId);
    }

    @Override
    public void stop() {
        Spider.Status status = this.getStatus();
        if (status.equals(Spider.Status.Running)) {
            super.stop();
            SPIDER_BUCKET.remove(this.hunterId);
        } else if (status.equals(Spider.Status.Init)) {
            throw new HunterException("爬虫正在初始化！HunterId：[" + this.hunterId + "]");
        } else {
            throw new HunterException("当前没有正在运行的爬虫！HunterId：[" + this.hunterId + "]");
        }
    }
}
