package com.zbf.web;

import com.zbf.common.ResponseResult;
import com.zbf.core.CommonUtils;
import com.zbf.core.page.Page;
import com.zbf.core.utils.UID;
import com.zbf.enmu.MyRedisKey;
import com.zbf.service.ShiJuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("shijuan")
@RestController
public class ShiJuanGuanLiController {
    @Autowired
    private ShiJuanService ShiJuanService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 添加
     * @return
     */
    @RequestMapping("toaddShiJuan")
    public ResponseResult toaddShiJuan(HttpServletRequest request){


        ResponseResult responseResult = ResponseResult.getResponseResult();

        //获取前台数据
        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap(request);

        //存入数据
        try {
            //添加自动生成的UUID
            paramsJsonMap.put("id", UID.getUUIDOrder () );
            ShiJuanService.toaddShiJuan(paramsJsonMap,redisTemplate);
            responseResult.setSuccess ( "ok" );
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setError ( "error" );
            //删除上一步骤中写入的数据
            redisTemplate.opsForHash ().delete ( MyRedisKey.SHIJUANFENLEI.getKey (),paramsJsonMap.get ( "id" ).toString ());
        }

        return responseResult;


    }

    @RequestMapping("getShiJunFenLeiList")
    public ResponseResult getShiJunFenLeiList(HttpServletRequest request){



        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( request );

        Page<Map<String,Object>> page=new Page<> ();

        ResponseResult responseResult=ResponseResult.getResponseResult ();
        //设置查询参数
        page.setParams ( paramsJsonMap );

        Page.setPageInfo ( page, paramsJsonMap);
        //
        ShiJuanService.getShiJunFenLeiList(page);


        responseResult.setResult ( page );

        return responseResult;

    }

    @RequestMapping("updateShiJuanFenLei")
    public ResponseResult updateShiJuanFenLei(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();
        //获取前台数据
        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap(request);
        //在数据库修改的同时，redis中的缓存也要更新
        ShiJuanService.updateShiJuanFenLei(paramsJsonMap,redisTemplate);

        //返回的响应
        responseResult.setSuccess("ok");

        return responseResult;


    }





}
