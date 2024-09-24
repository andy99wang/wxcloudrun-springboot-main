package com.tencent.wxcloudrun.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tencent.wxcloudrun.config.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson.JSON;

/**
 * 门户接口转发,通用get请求，post body传参
 */
//@CrossOrigin
@RestController
//@RequestMapping("/app/mhzf")
public class MhzfController {
	private String baseUrl="http://101.200.204.27/kskj-boot";
	@RequestMapping(path = "/**",method = RequestMethod.GET)
	public ApiResponse get(HttpServletRequest request) {
		//获取uri
		String uri = request.getRequestURI();
		uri = uri.substring(9);
		//获取参数
		Map<String,String[]> params = request.getParameterMap();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(params!=null){
			for(String key :params.keySet()){
				String[] param = params.get(key);
				if(param!=null&&param.length>0){
					paramMap.put(key, param[0]);
				}
			}
		}
		String result =  HttpUtil.get(baseUrl+uri, paramMap);
		return ApiResponse.ok(result);
	}
	@RequestMapping(path = "/**",method = RequestMethod.POST)
	public ApiResponse post(HttpServletRequest request,@RequestBody String jsonData) {
		String uri = request.getRequestURI();
		uri = uri.substring(9);
		String urlParam = "";
		//获取url拼接参数
		Map<String,String[]> params = request.getParameterMap();
		Map<String,Object> vueParam = new HashMap<String, Object>();
		if(params!=null){
			urlParam+="?aa=1";//方便拼接参数
			for(String key :params.keySet()){
				String[] param = params.get(key);
				if(param!=null&&param.length>0){
					urlParam+="&"+key+"="+ param[0];
					vueParam.put(key, param[0]);
				}
			}
		}
		//这种方式后台servlet 根据 request.getParameterMap()接收不到
		//String result = HttpUtil.post(baseUrl+uri+urlParam, jsonData);
		String result ="";
		if(jsonData!=null&&jsonData.startsWith("{") && jsonData.endsWith("}")){
			 result = HttpUtil.post(baseUrl+uri+urlParam, (Map)JSON.parse(jsonData));
		}else{//针对vue转发，特殊处理
			 result = HttpUtil.post(baseUrl+uri, vueParam);
		}
		return ApiResponse.ok(result);
	}

}
