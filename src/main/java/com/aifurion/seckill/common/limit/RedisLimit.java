package com.aifurion.seckill.common.limit;

import com.aifurion.seckill.common.utils.RedisPool;
import com.aifurion.seckill.common.utils.ScriptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 13:37
 */

@Slf4j
@Component
public class RedisLimit {

    private static final int FAIL_CODE = 0;

    @Value("${redis.limit}")
    private Integer limit;

    /**
     * Redis 限流
     */
    public Boolean limit() {

        Jedis jedis = null;
        long result = 0;
        try {
            // 获取 jedis 实例
            jedis = RedisPool.getJedis();
            // 解析 Lua 文件
            String script = ScriptUtil.getScript("limit.lua");
            // 请求限流
            String key = String.valueOf(System.currentTimeMillis() / 1000);
            // 计数限流
            result = (long) jedis.eval(script, Collections.singletonList(key),
                    Collections.singletonList(String.valueOf(limit)));
            if (FAIL_CODE != result) {
                log.info("成功获取令牌");
                return true;
            }
        } catch (Exception e) {
            log.error("limit 获取 Jedis 实例失败：", e);
        } finally {
            RedisPool.jedisPoolClose(jedis);
        }
        return false;
    }
}
