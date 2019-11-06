package com.leatop.bee.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ES配置
 * 
 * @author hongSheng
 *
 */
public class ElasticSearchConfiguration {
	
	private static String hosts;
	private static String ports;
	private static Integer connectTimeOut;
	private static Integer socketTimeOut;
	private static Integer connectionRequestTimeOut;
	private static Integer maxConnectNum;
	private static Integer maxConnectPerRoute;
	
	static {
	 	Properties prop =  new  Properties();  
        InputStream in = Object.class.getResourceAsStream( "/bee-data-elasticsearch.properties" );  
         try  {  
            prop.load(in);  
            hosts = prop.getProperty( "bee.data.elasticsearch.hosts" ).trim();  
            ports = prop.getProperty( "bee.data.elasticsearch.ports" ).trim();
            String connectTimeOut_Str = prop.getProperty( "bee.data.elasticsearch.connectTimeOut" ).trim();
            if(connectTimeOut_Str.length()!=0) {
            	 connectTimeOut = new Integer(connectTimeOut_Str);  
            }
            String socketTimeOut_Str = prop.getProperty( "bee.data.elasticsearch.socketTimeOut" ).trim();
            if(socketTimeOut_Str.length()!=0) {
            	socketTimeOut = new Integer(socketTimeOut_Str);
            }
            String connectionRequestTimeOut_Str = prop.getProperty( "bee.data.elasticsearch.socketTimeOut" ).trim();
            if(connectionRequestTimeOut_Str.length()!=0) {
            	connectionRequestTimeOut = new Integer(connectionRequestTimeOut_Str);  
            }
            String maxConnectNum_Str = prop.getProperty( "bee.data.elasticsearch.maxConnectNum" ).trim();
            if(maxConnectNum_Str.length()!=0) {
            	maxConnectNum = new Integer(maxConnectNum_Str);
            }
            String maxConnectPerRoute_STr = prop.getProperty( "bee.data.elasticsearch.maxConnectPerRoute" ).trim();
            if(maxConnectPerRoute_STr.length()!=0) {
            	maxConnectPerRoute = new Integer(maxConnectPerRoute_STr);
            }
            
              
        }  catch  (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	public String getHosts() {
		return hosts;
	}
	public String getPorts() {
		return ports;
	}
	public Integer getConnectTimeOut() {
		return connectTimeOut;
	}
	public Integer getSocketTimeOut() {
		return socketTimeOut;
	}
	public Integer getConnectionRequestTimeOut() {
		return connectionRequestTimeOut;
	}
	public Integer getMaxConnectNum() {
		return maxConnectNum;
	}
	public Integer getMaxConnectPerRoute() {
		return maxConnectPerRoute;
	}
	public ElasticSearchConfiguration() {
		super();
	}
	@Override
	public String toString() {
		return "ElasticSearchConfiguration [hosts=" + hosts + ", ports=" + ports
				+ ", connectTimeOut=" + connectTimeOut + ", socketTimeOut=" + socketTimeOut
				+ ", connectionRequestTimeOut=" + connectionRequestTimeOut + ", maxConnectNum="
				+ maxConnectNum + ", maxConnectPerRoute=" + maxConnectPerRoute + "]";
	}
	
}
