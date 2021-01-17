package com.springboot.redis.cluster.client.config.redis;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class ShareJedisClusterConfig {

    @Autowired
    ShareClusterRedisProperties redisProperties;
    /**
     * 注意：
     * 这里返回的JedisCluster是单例的，并且可以直接注入到其他类中去使用
     * @return
     */
    @Bean("shareJedisCluster")
    public JedisCluster getJedisCluster() {
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : redisProperties.getClusterNodes()) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        //需要密码连接的创建对象方式
        if (StringUtils.isEmpty(redisProperties.getPassword())) {
            return new JedisCluster(nodes,redisProperties.getRedisTimeout(),1000,1,new GenericObjectPoolConfig());
        } else {
            return new JedisCluster(nodes,redisProperties.getRedisTimeout(),1000,1,redisProperties.getPassword() ,new GenericObjectPoolConfig());
        }
    }
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisProperties.getPoolMaxActive());
        poolConfig.setMaxIdle(redisProperties.getPoolMaxIdle());
        poolConfig.setMinIdle(redisProperties.getPoolMinIdle());
        poolConfig.setMaxWaitMillis(redisProperties.getPoolMaxWait());
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(poolConfig)
                .and()
                .readTimeout(Duration.ofMillis(redisProperties.getRedisTimeout()))
                .build();
        // cluster模式
        RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
        redisConfig.setMaxRedirects(redisProperties.getClusterMaxRedirects());
        for (String ipPort :redisProperties.getClusterNodes()){
            String[] ipPortArr = ipPort.split(":");
            redisConfig.clusterNode(ipPortArr[0], Integer.parseInt(ipPortArr[1].trim()));
        }
        return new JedisConnectionFactory(redisConfig, clientConfig);
    }


}
