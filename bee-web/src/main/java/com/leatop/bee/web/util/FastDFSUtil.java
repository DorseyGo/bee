package com.leatop.bee.web.util;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FastDFS的工具方法
 * 
 * @author hongSheng
 *
 */
public class FastDFSUtil {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(FastDFSUtil.class);
	
	//fastdfs配置文件
	private final static String fdfsConfig = "fdfs_client.conf";
	
	//初始化配置
	static {
		try {
			ClientGlobal.init(fdfsConfig);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 	通过字节上传文件到fastDFS中
	 * 
	 * @param fileBuff
	 * @param file_Name
	 * @param fileExtName
	 * @return
	 */
	public static String[] uploadFile(byte[] fileBuff, String file_Name,String fileExtName) {
		String[] files = null;
		try {
			// 建立连接
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient client = new StorageClient(trackerServer, storageServer);

			// 设置元信息
			//NameValuePair[] metaList = new NameValuePair[3];
			//metaList[0] = new NameValuePair("fileName", file_Name);
			//metaList[1] = new NameValuePair("fileExtName", fileExtName);
			//metaList[2] = new NameValuePair("fileLength",String.valueOf(fileBuff.length));
			
			// 上传文件
			files = client.upload_file(fileBuff, fileExtName, null);
			trackerServer.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		return files;
	}

}
