package com.zbf.service;

import com.zbf.core.page.Page;
import com.zbf.enmu.MyRedisKey;
import com.zbf.mapper.ShiJuanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ShiJuanService {
    @Autowired
    private ShiJuanMapper shiJuanMapper;

    public int toaddShiJuan(Map<String, Object> paramsJsonMap, RedisTemplate redisTemplate) {
        //向redis中写入数据使用 Map数据类型
        redisTemplate.opsForHash().put(MyRedisKey.SHIJUANFENLEI.getKey(),paramsJsonMap.get("id").toString(),paramsJsonMap);

        return shiJuanMapper.toaddShiJuan ( paramsJsonMap );
    }

    public void getShiJunFenLeiList(Page<Map<String, Object>> page) {
        List<Map<String,Object>> list=shiJuanMapper.getShiJunFenLeiList(page);
        list.forEach ( (item)->{
            if(item.get ( "fenleizhuangtai" ).toString ().equals ( "1" )){
                item.put ( "fenleizhuangtai","正常");
            }else{
                item.put ( "fenleizhuangtai","锁定");
            }
        } );

        page.setResultList(list);
    }

    public void updateShiJuanFenLei(Map<String, Object> paramsJsonMap, RedisTemplate redisTemplate) {
        //更新redis
        redisTemplate.opsForHash().delete(MyRedisKey.SHIJUANFENLEI.getKey(),paramsJsonMap.get("id").toString(),paramsJsonMap);

        shiJuanMapper.updateShiJuanFenLei(paramsJsonMap);
    }
}
