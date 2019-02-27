package com.zbf.mapper;

import com.zbf.core.page.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShiJuanMapper {
    public  int toaddShiJuan(Map<String, Object> paramsJsonMap);


    public List<Map<String, Object>> getShiJunFenLeiList(Page<Map<String, Object>> page);

    public int  updateShiJuanFenLei(Map<String, Object> paramsJsonMap);
}
