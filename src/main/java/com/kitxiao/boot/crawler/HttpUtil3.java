package com.kitxiao.boot.jd_gpuprice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * @项目名称：com-dealShop-spider
 * @类名称：HttpUtil3
 * @类描述：okhttp3。9
 * @创建人：白轲祥
 * @创建时间：2018年2月8日 下午4:07:29
 */
public class HttpUtil3 {
	
	private static final OkHttpClient CLIENT = new OkHttpClient();
	
	private static CookieJar cookieJar = new CookieJar() {
		// Cookie缓存区
		private final Map<String, List<Cookie>> cookiesMap = new HashMap<String, List<Cookie>>();
		@Override
		public void saveFromResponse(HttpUrl arg0, List<Cookie> arg1) {
			// 移除相同的url的Cookie
			String host = arg0.host();
			List<Cookie> cookiesList = cookiesMap.get(host);
			if (cookiesList != null) {
				cookiesMap.remove(host);
			}
			// 再重新天添加
			cookiesMap.put(host, arg1);
		}
		@Override
		public List<Cookie> loadForRequest(HttpUrl arg0) {
			List<Cookie> cookiesList = cookiesMap.get(arg0.host());
			// 注：这里不能返回null，否则会报NULLException的错误。
			// 原因：当Request 连接到网络的时候，OkHttp会调用loadForRequest()
			return cookiesList != null ? cookiesList : new ArrayList<Cookie>();
		}
	};
	
	public static String get(String url) throws Exception {
		String data = "";
		try {
			Request request = new Request.Builder().url(url).build();
			okhttp3.Response response = CLIENT.newCall(request).execute();
			data = response.body().string();
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
	
	public static String get(String url,String referer) throws Exception {
		String data = "";
		try {
			Request request = new Request.Builder().url(url).header("Referer", referer).build();
			okhttp3.Response response = CLIENT.newCall(request).execute();
			data = response.body().string();
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
	
	public static String get(String url,Map<String, String> headerMap) throws Exception {
		Headers headers = null;
		if(headerMap != null){
			okhttp3.Headers.Builder hBuilder = new okhttp3.Headers.Builder();
			hBuilder.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat");
			hBuilder.add("Accept-Language", "zh-CN,zh;q=0.8,en-us;q=0.6,en;q=0.5;q=0.4");
			Iterator<String> iterator = headerMap.keySet().iterator();  
		    String key = "";  
		    while (iterator.hasNext()) {
		        key = iterator.next().toString();  
		        hBuilder.add(key, headerMap.get(key));  
		    }
		    headers = hBuilder.build();
		}
		String data = "";
		try {
			Request request = new Request.Builder().url(url).headers(headers).build();
			okhttp3.Response response = CLIENT.newCall(request).execute();
			data = response.body().string();
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
	
	public static String getGBK(String url) throws Exception {
		String data = "";
		try {
			Request request = new Request.Builder().url(url).build();
			okhttp3.Response response = CLIENT.newCall(request).execute();
			byte[] bytes = response.body().bytes(); //获取数据的bytes
			data = new String(bytes, "GBK");     // 指定格式转换字符串
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
	
	// 设置代理访问
	public static String get(String url,String ip,int port) throws Exception {
		String data = "";
		Proxy proxy = null;
		try {
			if(ip!=null&&port>0){
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
			}
			OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MILLISECONDS).proxy(proxy).build();
			Request request = new Request.Builder().url(url).build();
			okhttp3.Response response = client.newCall(request).execute();
			data = response.body().string();
		} catch (Exception e) {
			return data;
		}
		return data;
	}
	
	public static String get(String url,Map<String, Object> dataMap,Map<String, String> headerMap) throws Exception {
		StringBuilder builder=new StringBuilder(url);
		Headers headers = null;
		if(dataMap != null){
			if (null!=dataMap&&!dataMap.isEmpty()) {
				Iterator<String> iterator=dataMap.keySet().iterator();
				int index = 0;
				String key = "";
				while (iterator.hasNext()) {
					key = iterator.next();
					String value="";
					Object valueTemp=dataMap.get(key);
					if (null==valueTemp) {						//值为空
						continue;
					}else if (valueTemp instanceof Number) {	//值是数字(int，double等等)
						value+=valueTemp;
					}else if(valueTemp instanceof String) {		//值是字符串
						value=(String)valueTemp;
					}else {										//其他，忽略
						continue;
					}
					key=key.trim();
					value=value.trim();
					if (0==index) {
						builder.append("?").append(key).append("=").append(value);
					}else {
						builder.append("&").append(key).append("=").append(value);	
					}
					index++;
				}
			}
		}
		if(headerMap != null){
			okhttp3.Headers.Builder hBuilder = new okhttp3.Headers.Builder();
			hBuilder.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat");
			hBuilder.add("Accept-Language", "zh-CN,zh;q=0.8,en-us;q=0.6,en;q=0.5;q=0.4");
			Iterator<String> iterator = headerMap.keySet().iterator();  
		    String key = "";  
		    while (iterator.hasNext()) {
		        key = iterator.next().toString();  
		        hBuilder.add(key, headerMap.get(key));  
		    }
		    headers = hBuilder.build();
		}
		String data = "";
		try {
			Request request = new Request.Builder().url(builder.toString()).headers(headers).build();
			okhttp3.Response response = CLIENT.newCall(request).execute();
			data = response.body().string();
		} catch (Exception e) {
			return null;
		}
		return data;
	}
	
	/**
	 * 下载图片，返回byte[]
	 */
	public static byte[] getPicutre(String url) throws Exception {
		byte[] data;
		try {
			Request request = new Request.Builder().url(url).build();
			okhttp3.Response response = CLIENT.newCall(request).execute();
			data=response.body().bytes();
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
	
	public static String post(String url,String jsonData) throws Exception {
		RequestBody body=RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
		String data = "";
		try {
			Request request = new Request.Builder().url(url).post(body).build();
			okhttp3.Response response = CLIENT.newCall(request).execute();
			data = response.body().string();
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
	
	public static String post(String url,byte[] content) throws Exception {
		RequestBody body=RequestBody.create(MediaType.parse("multipart/form-data;"), content);
		String data = "";
		try {
			Request request = new Request.Builder().url(url).post(body).build();
			okhttp3.Response response = CLIENT.newCall(request).execute();
			data = response.body().string();
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
	
	public static String post(String url,Map<String, Object> dataMap) throws Exception {
		Builder builder= new FormBody.Builder();
		if (null!=dataMap&&!dataMap.isEmpty()) {
			Iterator<String> iterator=dataMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key=iterator.next();
				String value="";
				Object valueTemp=dataMap.get(key);
				if (null==valueTemp) {						//值为空
					continue;
				}else if (valueTemp instanceof Number) {	//值是数字(int，double等等)
					value+=valueTemp;
				}else if(valueTemp instanceof String) {		//值是字符串
					value=(String)valueTemp;
				}else {										//其他，忽略
					continue;
				}
				key=key.trim();
				value=value.trim();
				builder.add(key, value);
			}
		}
		FormBody body=builder.build();
		String data = "";
		try {
			Request request = new Request.Builder().url(url).post(body).build();
			okhttp3.Response response = CLIENT.newCall(request).execute();
			data = response.body().string();
		} catch (Exception e) {
			throw e;
		}
		return data;
	}
	
	public static String getCookie(String url,Map<String, Object> dataMap) throws Exception{
		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MILLISECONDS).cookieJar(cookieJar).build();
		Builder builder= new FormBody.Builder();
		if (null!=dataMap&&!dataMap.isEmpty()) {
			Iterator<String> iterator=dataMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key=iterator.next();
				String value="";
				Object valueTemp=dataMap.get(key);
				if (null==valueTemp) {						//值为空
					continue;
				}else if (valueTemp instanceof Number) {	//值是数字(int，double等等)
					value+=valueTemp;
				}else if(valueTemp instanceof String) {		//值是字符串
					value=(String)valueTemp;
				}else {										//其他，忽略
					continue;
				}
				key=key.trim();
				value=value.trim();
				builder.add(key, value);
			}
		}
		FormBody body = builder.build();
		String cookie = "";
		try {	
			Request request = new Request.Builder().url(url).post(body).build();
			okhttp3.Response response = client.newCall(request).execute();
			cookie = response.networkResponse().request().headers().get("Cookie");
		} catch (Exception e) {
			throw e;
		}
		return cookie;
	}
	
}
