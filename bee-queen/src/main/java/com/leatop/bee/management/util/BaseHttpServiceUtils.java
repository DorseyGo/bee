package com.leatop.bee.management.util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
/**
 * http请求工具类
 * 
 * @author zlm
 *
 */
public class BaseHttpServiceUtils {
	public static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BaseHttpServiceUtils.class);
	
	public static void proxyProcess(HttpURLConnection con,HttpServletResponse response){
		try {
			logger.info("URL:"+con.getURL()+"({} {})",con.getResponseCode(),con.getResponseMessage());
			if(con.getResponseCode() == 200){
				try{
					putStream(response,con.getInputStream());
				}
				catch(IllegalStateException e){
					putStreamByWrite(response,con.getInputStream());
				}
				
			}else{
				response.getWriter().write("{\"success\":false}");
			}

		} catch (Exception e) {
			logger.error("HttpRequest failed:"+con.getURL()+"("+e.getMessage()+")",e);
			try {
				response.getWriter().write("{\"success\":false,\"msg\":\""+e.getMessage()+"\"}");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}finally{
			if(con!=null){
				con.disconnect();
			}
		}
	}
	
	/**
	 * 参数处理
	 * 
	 * @param obj
	 * @return
	 */
	public static String convertParams(Object obj){
		StringBuffer params = null;
		try {
			Class<?> clazz = obj.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(obj);
				if (value != null) {
					String _value = URLEncoder
							.encode(value.toString(), "UTF-8");
					if (params == null) {
						params = new StringBuffer();
						params.append(field.getName() + "=" + _value);
					} else {
						params.append("&" + field.getName() + "=" + _value);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error!", e);
		}
		return params.toString();
	}
	
	//转换成适配controller自动注入的参数
	public static String convertObjParams(Object obj){
		StringBuffer params = null;
		try {
			Class<?> clazz = obj.getClass();
			Method[] methods = clazz.getMethods();
			
			for(Method mt : methods)
			{
				if(mt.getName().startsWith("get")&&!mt.getName().equals("getClass"))
				{
					Object vobj = mt.invoke(obj);
					if(vobj!=null)
					{
						String value = URLEncoder.encode(vobj.toString(), "UTF-8");
						if(params == null)
						{
							params = new StringBuffer();
							params.append(mt.getName().replaceAll("get", "")+"="+value);
						}
						else
						{
							params.append("&"+mt.getName().replaceAll("get", "")+"="+value);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error!", e);
		}
		return params.toString();
	}
	
	/**
	 * doPost提交
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static HttpURLConnection doPost(java.net.URL url,String  params){
		
			HttpURLConnection conn  = null;
			try{
			conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoOutput(true);// 是否输入参数
			conn.setConnectTimeout(3000);// 连接超时时间（不包括数据传输时间）
			conn.setRequestMethod("POST");
			conn.setRequestProperty(
							"Accept",
							"text/html, application/xhtml+xml, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty(
							"User-Agent",
							"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("contentType", "UTF-8");
			
			byte[] bypes = params.getBytes("UTF-8");
			
			conn.getOutputStream().write(bypes);
			}
			catch(ConnectException e){
			}
			catch(Exception e){
				logger.error("HttpRequest Error:",e);
			}
			return conn;
		}
	
	public static HttpURLConnection doPost(java.net.URL url,byte[]  params){
		
		HttpURLConnection conn  = null;
		try{
		conn = (HttpURLConnection) url.openConnection();
		conn.setUseCaches(false);
		conn.setDoOutput(true);// 是否输入参数
		conn.setConnectTimeout(3000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty(
						"Accept",
						"text/html, application/xhtml+xml, */*");
		conn.setRequestProperty("Accept-Language", "zh-CN");
		conn.setRequestProperty(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestProperty("contentType", "UTF-8");
		
		conn.getOutputStream().write(params);
		}
		catch(ConnectException e){
		}
		catch(Exception e){
			logger.error("HttpRequest Error:",e);
		}
		return conn;
	}
	
	/**
	 * 写文件
	 * 
	 * @param ins
	 * @param file
	 * @return
	 */
	public static void readStreamToFile(InputStream ins,File file) throws IOException{
	   OutputStream os = new FileOutputStream(file);
	   int bytesRead = 0;
	   byte[] buffer = new byte[8192];
	   while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
	      os.write(buffer, 0, bytesRead);
	   }
	   os.close();
	   ins.close();
	}
	
	/**
	 * stream流输出
	 * 
	 * @param response
	 * @param inStream
	 * @return
	 */
	public static void putStream(HttpServletResponse response,InputStream inStream) throws Exception {
		ServletOutputStream servletOutputStream  = response.getOutputStream();
		byte[] buffer = new byte[102400];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			servletOutputStream.write(buffer, 0, len);
		}
		inStream.close();
		servletOutputStream.close();
	}
	
	/**
	 * stream流输出by writer
	 * 
	 * @param response
	 * @param inStream
	 * @return
	 */
	public static void putStreamByWrite(HttpServletResponse response,InputStream inStream) throws Exception {
		 BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));   
	     StringBuilder sb = new StringBuilder();   
	     String line = null;   
	     try {   
	         while ((line = reader.readLine()) != null) {   
	             sb.append(line);   
	         }   
	         response.getWriter().write(sb.toString());
	     } catch (IOException e) {   
	         e.printStackTrace();   
	     } finally {   
	         try {   
	        	 inStream.close();   
	         } catch (IOException e) {   
	             e.printStackTrace();   
	         }   

	     }   
	}
	
	/**
	 * 读取stream流
	 * 
	 * @param inStream
	 * @return
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[102400];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
	
	/**
	 * 参数封装
	 * 
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static byte[] readParameters(Map  parameters) throws Exception {
		StringBuffer params = new StringBuffer();
		for(Object key : parameters.keySet()){
			Object paramvalue = parameters.get(key);
			if(paramvalue!=null){
				if(paramvalue instanceof String[]){
					for(String param : (String[])paramvalue){
						String value = URLEncoder.encode(param, "UTF-8");
						params.append((params.length() == 0?"":"&")+key+"="+value);
					}
				}else{
					String value = URLEncoder.encode(paramvalue.toString(), "UTF-8");
					params.append((params.length() == 0?"":"&")+key+"="+value);
				}
			}
		}
		
		return params.toString().getBytes("UTF-8");
	}
	
	/**
	 * 参数封装
	 * 
	 * @param parameters
	 * @return
	 */
	public static String readParametersStr(Map  parameters) throws Exception {
		StringBuffer params = new StringBuffer();
		for(Object key : parameters.keySet()){
			Object paramvalue = parameters.get(key);
			if(paramvalue!=null){
				if(paramvalue instanceof String[]){
					for(String param : (String[])paramvalue){
						String value = URLEncoder.encode(param, "UTF-8");
						params.append((params.length() == 0?"":"&")+key+"="+value);
					}
				}else{
					String value = URLEncoder.encode(paramvalue.toString(), "UTF-8");
					params.append((params.length() == 0?"":"&")+key+"="+value);
				}
			}
		}
		return params.toString();
	}
}
