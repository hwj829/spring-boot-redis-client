package com.springboot.redis.cluster.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisController {

    private final JedisCluster jedisCluster;

    @RequestMapping(value = "v1/redis/get",method = RequestMethod.GET)
    public String getValue(@RequestParam(value = "key", required = false)String key){
        try{
            String value = jedisCluster.get(key);
            return value;
        }catch (Exception e){
            e.printStackTrace();
            return "failed";
        }
    }

    @RequestMapping(value = "v1/redis/set",method = RequestMethod.GET)
    public String setValue(@RequestParam(value = "key", required = false)String key, @RequestParam(value = "value", required = false)String value){
        if(StringUtils.hasLength(key) || StringUtils.hasLength(value)){
            return "Error:key or value is null";
        }
        try{
            jedisCluster.set(key, value);
        }catch (Exception e){
            e.printStackTrace();
            return "failed";
        }
        return "successful";
    }

    @RequestMapping(value = "v1/redis/say",method = RequestMethod.GET)
    public String say(@RequestParam(value = "name", required = false)String name){
        return "Hi " + name;
    }

}
