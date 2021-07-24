package cn.yongqiang.constant;

import java.util.concurrent.TimeUnit;

public interface RedisConstant {
    /**
     * 设置过期时间一天，与TIME_UNIT配合
     * 60*60*24
     */
    Long DAY_TIME = 86400L;

    /**
     * 过期时间单位，与TIME配合
     */
    TimeUnit TIME_UNIT = TimeUnit.SECONDS;
}
