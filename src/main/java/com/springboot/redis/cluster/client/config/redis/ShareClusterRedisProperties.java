package com.springboot.redis.cluster.client.config.redis;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class ShareClusterRedisProperties {

    @Value("${share.redis.timeout}")
    private Integer redisTimeout;
    @Value("${share.redis.jedis.pool.max-active}")
    private Integer poolMaxActive;
    @Value("${share.redis.jedis.pool.max-idle}")
    private Integer poolMaxIdle;
    @Value("${share.redis.jedis.pool.min-idle}")
    private Integer poolMinIdle;
    @Value("${share.redis.jedis.pool.max-wait}")
    private Integer poolMaxWait;
    @Value("${share.redis.cluster.nodes}")
    private List<String> clusterNodes;
    @Value("${share.redis.cluster.max-redirects}")
    private Integer clusterMaxRedirects;
    @Value("${share.redis.password}")
    private String password;

}
