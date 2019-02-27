package com.zbf.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbf.common.ResponseResult;
import com.zbf.core.CommonUtils;
import com.zbf.core.page.Page;
import com.zbf.core.utils.FileUploadDownUtils;
import com.zbf.core.utils.UID;
import com.zbf.enmu.MyRedisKey;
import com.zbf.service.TiKuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * 作者：LCG
 * 创建时间：2019/2/14 11:00
 * 描述：
 */
@RequestMapping("tiku")
@RestController
public class TiKuGuanLiController {
    @Autowired
    private TiKuService tiKuService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加题库信息
     * @param request
     * @return
     */
    @RequestMapping("toaddTiKuInfo")
    public ResponseResult toaddTiKuInfo(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        //获取数据
        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap ( request );
        //存入数据
        try {
            parameterMap.put ( "id",UID.getUUIDOrder () );
            tiKuService.addTiKuInfo ( parameterMap,redisTemplate);
            responseResult.setSuccess ( "ok" );
        }catch (Exception e){
            e.printStackTrace ();
            responseResult.setError ( "error" );
            //删除上一步骤中写入的数据
            redisTemplate.opsForHash ().delete ( MyRedisKey.TIKU.getKey (),parameterMap.get ( "id" ).toString ());
        }

        return responseResult;

    }

    /**
     * 题库列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("getTikuList")
    public ResponseResult getTikuList(HttpServletRequest httpServletRequest){

        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( httpServletRequest );

        Page<Map<String,Object>> page=new Page<> ();

        ResponseResult responseResult=ResponseResult.getResponseResult ();
        //设置查询参数
        page.setParams ( paramsJsonMap );

        Page.setPageInfo ( page, paramsJsonMap);

        //
        tiKuService.getTikuList ( page );
        //

        responseResult.setResult ( page );

        return responseResult;

    }
    @RequestMapping("deleteTiKuById")
    public ResponseResult deleteTiKuById(HttpServletRequest request){
        ResponseResult responseResult=ResponseResult.getResponseResult ();
        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( request );
        tiKuService.deleteTiKuById(paramsJsonMap,redisTemplate);
        responseResult.setSuccess ( "ok" );
        return responseResult;

    }

    /**
     * 更新题库信息
     * @param request
     * @return
     */
    @RequestMapping("updateTiKuInfo")
    public ResponseResult updateTiKuInfo(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        Map<String, Object> paramsJsonMap = CommonUtils.getParamsJsonMap ( request );

        tiKuService.updateTiKuInfo ( paramsJsonMap,redisTemplate );

        responseResult.setSuccess ( "ok" );

        return responseResult;
    }

    /**
     * 从redis中获取题库列表信息
     * @param request
     * @return
     */
    @RequestMapping("getTikuListFromRedis")
    public ResponseResult getTikuListFromRedis(HttpServletRequest request){

        List<Map<String,Object>> values = redisTemplate.opsForHash ().values ( MyRedisKey.TIKU.getKey () );

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        responseResult.setResult ( values );

        return responseResult;

    }

    /**
     * 手动添加试题
     * @return
     */
    @RequestMapping("toAddShiTi")
    public ResponseResult toAddShiTi(HttpServletRequest request) throws Exception {

        ResponseResult responseResult=ResponseResult.getResponseResult ();
        //获取请求数据
        Map<String, Object> parameterMap = CommonUtils.getParameterMap ( request );
        //写入数据
        tiKuService.addShitiInfo ( redisTemplate,parameterMap );

        responseResult.setSuccess ( "ok" );

        return responseResult;
    }

    /**
     *
     *  试题管理 试题列表
     * @param request
     * @return
     */
    @RequestMapping("togetShitiList")
    public ResponseResult togetShitiList(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap ( request );

        Page<Map<String,Object>> page=new Page<> ();

        Page.setPageInfo ( page,parameterMap );

        tiKuService.getShitiList ( page );

        responseResult.setResult ( page );

         return responseResult;
    }

    @RequestMapping("getExceltemplate")
    public void getExceltemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {

        File excelTemplate = FileUploadDownUtils.getExcelTemplate ( "exceltemplate/timu.xlsx" );

        FileUploadDownUtils.responseFileBuilder ( response,excelTemplate,"数据模板【题目】.xlsx" );

    }


    /**
     * 根据ID获取试题信息
     * @param request
     * @return
     */
    @RequestMapping("getShitiById")
    public ResponseResult getShitiById(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        Map<String, Object> parameterMap = CommonUtils.getParameterMap ( request );

        Map<String, Object> shiTiById = tiKuService.getShiTiById ( parameterMap );

        responseResult.setResult ( shiTiById );

        return responseResult;

    }

}
