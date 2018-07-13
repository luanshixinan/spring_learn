/**
 * Copyright &copy; 2015-2020 <a href="http://www.ztrk.org/">ztrk</a> All rights reserved.
 */
package org.spring.springboot.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName FastJsonRedisSer
 * @Description //TODO 描述类的功能
 * @Author zhouzhendong
 * @Date 2018/7/3 15:34
 * @Version V1.0
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

//    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, StandardCharsets.UTF_8);
        return (T) JSON.parseObject(str, clazz);
    }

}
