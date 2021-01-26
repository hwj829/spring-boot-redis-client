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

import java.io.*;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisController {

    private final JedisCluster jedisCluster;

    String path = "/apps-data/";

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


    @RequestMapping(value = "v1/write",method = RequestMethod.GET)
    public String write(@RequestParam(value = "content", required = true)String content){

        FileOutputStream fos= null;
        BufferedOutputStream bos = null;
        File file = new File(path + content.substring(0,content.length()));
        try {
            if(file.exists()){
                file.delete();
                file = new File(path + content.substring(0,content.length()));
            }
            fos = new FileOutputStream(file);
            bos=new BufferedOutputStream(fos);
            bos.write(content.getBytes(),0,content.getBytes().length);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "OK";
    }

    @RequestMapping(value = "v1/read",method = RequestMethod.GET)
    public String read(@RequestParam(value = "name", required = true)String name){

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        File file = new File(path + name);

        String content = "";
        try {
            if(!file.exists()){
                return "File not found.";
            }

            fis=new FileInputStream(file);
            bis=new BufferedInputStream(fis);

            //自定义缓冲区
            byte[] buffer=new byte[10240];
            int flag=0;
            while((flag=bis.read(buffer))!=-1){
                content+=new String(buffer, 0, flag);
            }
            System.out.println(content);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    @RequestMapping(value = "v1/list",method = RequestMethod.GET)
    public String[] list(@RequestParam(value = "path", required = true)String path){

        File file = new File(path);
        String[] files = null;
        try {
            if(file.isDirectory()){
                files = file.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

}
