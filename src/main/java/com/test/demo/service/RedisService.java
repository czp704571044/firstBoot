package com.test.demo.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisService {
    private RedisTemplate<String, Object> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key,long time){
        try {
            if(key != null && time>0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            Print.printException("RedisTool expire(String key,long time)",key,String.valueOf(time),e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key){
        try {
            if(key == null || key.length() < 1){
                return false;
            }else{
                return redisTemplate.hasKey(key);
            }
        } catch (Exception e) {
            Print.printException("RedisTool hasKey(String key)",key,e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String ... key){
        if(key != null && key.length >0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    //============================String=============================
    /**
     * 普通缓存获取;
     * @param key 键
     * @return 值
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public <T> T getObject(String key,Class<T> clazz){
        if(key == null){
            return null;
        }else{
            Object value = redisTemplate.opsForValue().get(key);
            return ( value == null) ? null : JsonUtil.returnObj(value.toString(), clazz);
        }
    }

    /**
     * 普通缓存获取;
     * @param key 键
     * @return 值
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public <T> List<T> getList(String key,Class<T> clazz){
        if(key == null){
            return null;
        }else{
            Object value = redisTemplate.opsForValue().get(key);
            return ( value == null) ? null : JsonUtil.toList(value.toString(), clazz);
        }
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public <T> T getObject(String key,Type type){
        if(key == null){
            return null;
        }else{
            Object value = redisTemplate.opsForValue().get(key);
            return ( value == null) ? null : JsonUtil.returnObj(value.toString(), type);
        }
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key,Object value) {
        try {
            if(key == null  || value == null ){
                return false;
            }else{
                redisTemplate.opsForValue().set(key, value);
                return true;
            }
        } catch (Exception e) {
            Print.printException("RedisTool set(String key,Object value)",key, JsonUtil.toJson(value),e);
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key,Object value,long time){
        try {
            if(key == null  || value == null){
                return false;
            }
            if( time > 0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            Print.printException("RedisTool set(String key,Object value,long time)",key, JsonUtil.toJson(value),String.valueOf(time),e);
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key,String item){
        if(key == null || key.length() <1){
            return null;
        }else{
            return redisTemplate.opsForHash().get(key, item);
        }

    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object,Object> hmget(String key){
        if(key == null || key.length() <1){
            return null;
        }else{
            return redisTemplate.opsForHash().entries(key);
        }
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String,Object> map){
        try {
            if(key == null || map == null || map.isEmpty()){
                return false;
            }else{
                redisTemplate.opsForHash().putAll(key, map);
                return true;
            }
        }catch (Exception e) {
            Print.printException("RedisTool hmset(String key, Map<String,Object> map)",key, JsonUtil.toJson(map),e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String,Object> map, long time){
        try {
            if(key ==null || map == null || map.isEmpty()){
                return false;
            }else{
                redisTemplate.opsForHash().putAll(key, map);
                if(time > 0){
                    expire(key, time);
                }
                return true;
            }
        } catch (Exception e) {
            Print.printException("RedisTool hmset(String key, Map<String,Object> map, long time)",key, JsonUtil.toJson(map),String.valueOf(time),e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key,String item,Object value) {
        try {
            if(key == null || value == null ){
                return false;
            }else{
                redisTemplate.opsForHash().put(key, item, value);
                return true;
            }
        } catch (Exception e) {
            Print.printException("RedisTool hset(String key,String item,Object value)",key,item, JsonUtil.toJson(value),e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key,String item,Object value,long time) {
        try {
            if(key == null || value == null ){
                return false;
            }else{
                redisTemplate.opsForHash().put(key, item, value);
                if(time>0){
                    expire(key, time);
                }
                return true;
            }
        } catch (Exception e) {
            Print.printException("RedisTool hset(String key,String item,Object value,long time)",key,item, JsonUtil.toJson(value),String.valueOf(time),e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item){
        redisTemplate.opsForHash().delete(key,item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item){
        if(key == null || item == null){
            return false;
        }else{
            return redisTemplate.opsForHash().hasKey(key, item);
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item,double by){
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item,double by){
        return redisTemplate.opsForHash().increment(key, item,-by);
    }

    //============================set=============================
    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key){
        try {
            if(key == null) {
                return null;
            }else{
                return redisTemplate.opsForSet().members(key);
            }
        } catch (Exception e) {
            Print.printException("RedisTool sGet(String key)",key,e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key,Object value){
        try {
            if( key == null || value == null ){
                return false;
            }else{
                return redisTemplate.opsForSet().isMember(key, value);
            }
        } catch (Exception e) {
            Print.printException("RedisTool sHasKey(String key,Object value)",key, JsonUtil.toJson(value),e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object...values) {
        try {
            if(key == null || values == null){
                return 0;
            }else{
                return redisTemplate.opsForSet().add(key, values);
            }
        } catch (Exception e) {
            Print.printException("RedisTool sSet(String key, Object...values)",key, JsonUtil.toJson(values),e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key,long time,Object...values) {
        try {
            if(key == null || values == null){
                return 0;
            }else{
                Long count = redisTemplate.opsForSet().add(key, values);
                if(time>0) expire(key, time);
                return count;
            }
        } catch (Exception e) {
            Print.printException("RedisTool sSet(sSetAndTime(String key,long time,Object...values)",key,String.valueOf(time), JsonUtil.toJson(values),e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key){
        try {
            if(key == null){
                return 0;
            }else{
                return redisTemplate.opsForSet().size(key);
            }
        } catch (Exception e) {
            Print.printException("RedisTool sGetSetSize(String key)",key,e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object ...values) {
        try {
            if(key == null || values == null){
                return 0;
            }else{
                return redisTemplate.opsForSet().remove(key, values);
            }
        } catch (Exception e) {
            Print.printException("RedisTool setRemove(String key, Object ...values)",key, JsonUtil.toJson(values),e);
            return 0;
        }
    }


    //===============================list=================================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return
     */
    public <T> List<T> lRange(String key,Class<T> clazz,long start, long end){
        try {
            if(key == null){
                return null;
            }else{
                return JsonUtil.redisToList(redisTemplate.opsForList().range(key, start, end), clazz) ;
            }
        } catch (Exception e) {
            Print.printException("RedisTool lGet(String key,long start, long end)",key,String.valueOf(start),String.valueOf(end),e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetSize(String key){
        try {
            if(key == null){
                return 0;
            }else{
                return redisTemplate.opsForList().size(key);
            }
        } catch (Exception e) {
            Print.printException("RedisTool lGetListSize(String key)",key,e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key,long index){
        try {
            if(key == null ){
                return null;
            }else{
                return redisTemplate.opsForList().index(key, index);
            }
        } catch (Exception e) {
            Print.printException("RedisTool lGetIndex(String key,long index)",key,e);
            return null;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引
     * @return 指定对象
     */
    public <T> T lGetObjectIndex(String key,long index,Class<T> clz){
        try {
            if(key == null){
                return null;
            }else{
                Object object = lGetIndex(key, index);
                return object == null ? null :JsonUtil.returnObj(object.toString(),clz);
            }
        } catch (Exception e) {
            Print.printException("RedisTool lGetObjectIndex(String key)",key,e);
            return null;
        }
    }


    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引
     * @return list<T>
     */
    public <T> List<T> lGetListIndex(String key,long index,Class<T> clz){
        try {
            if(key == null ){
                return null;
            }else{
                Object object = lGetIndex(key, index);
                return object == null ? null :JsonUtil.toList(object.toString(),clz);
            }
        } catch (Exception e) {
            Print.printException("RedisTool lGetListIndex(String key)",key,e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lPush(String key, Object value, long time) {
        try {
            if(key == null || value == null ){
                return false;
            }else{
                redisTemplate.opsForList().rightPush(key, value);
                if (time > 0) expire(key, time);
                return true;
            }
        } catch (Exception e) {
            Print.printException("RedisTool lSet(String key, Object value, long time)",key, JsonUtil.toJson(value),String.valueOf(time),e);
            return false;
        }

    }



    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key,long count,Object value) {
        try {
            if(key == null){
                return 0;
            }else{
                return redisTemplate.opsForList().remove(key, count, value);
            }
        } catch (Exception e) {
            Print.printException("RedisTool lRemove(String key,long count,Object value)",key,String.valueOf(count), JsonUtil.toJson(value),e);
            return 0;
        }
    }

    /**
     * 清空list
     * @param key 键
     * @param var1 移除多少个
     * @param var2 值
     * @return 测试(key,1,0)清空list
     */
    public void lTrim(String key,long var1,long var2) {
        try {
            if(key == null){
                return ;
            }
            redisTemplate.opsForList().trim(key,var1,var2);
        } catch (Exception e) {
            Print.printException("RedisTool lTrim(String key,long var1,long var2)",key,e);
        }
    }


    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}
