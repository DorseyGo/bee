package com.leatop.bee.management.util;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import com.leatop.bee.common.log.LogFacade;


/**
 * FastDFS的工具方法
 * 
 * @author hongSheng
 *
 */
public class FastDFSUtil {

	private static LogFacade LOGGER = LogFacade.getFacade(FastDFSUtil.class);
	private final static String fdfsConfig = "fdfs_client.conf";

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
		TrackerServer trackerServer = null;
		StorageServer storageServer = null;
		//trackerServer,storageServer,StorageClient1成员是共享的之后会出问题，所以每次都重新创建
		try {
			ClientGlobal.init(fdfsConfig);
			// 建立连接
			TrackerClient tracker = new TrackerClient();
			trackerServer = tracker.getConnection();
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
			try {
				trackerServer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			LOGGER.error("Upload file \"" + file_Name + "\"fails");
		}
		return files;
	}

}
