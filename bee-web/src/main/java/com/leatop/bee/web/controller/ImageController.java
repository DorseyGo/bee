package com.leatop.bee.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.leatop.bee.web.conf.BeeWebConfiguration;
import com.leatop.bee.web.resp.Resp;
import com.leatop.bee.web.service.ImageWeaveInManager;

/**
 * 	图片上传接口类
 * 
 * @author hongSheng
 *
 */
@RestController
@RequestMapping("/images")
public class ImageController {
	
	private static final Logger LOGGER = Logger.getLogger(ImageController.class.getName());
	
	private final static String fdfsConfig="fdfs_client.conf";
	
	// ~~~ fields
	// ==================================================
	private final ImageWeaveInManager imageWeaveInManager;
	private final BeeWebConfiguration config;

	// ~~~ constructors
	// ==================================================
	@Autowired
	public ImageController(final ImageWeaveInManager imageWeaveInManager,
			final BeeWebConfiguration config) {
		this.imageWeaveInManager = imageWeaveInManager;
		this.config = config;
		try {
			ClientGlobal.init(fdfsConfig);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ~~~ methods
	// ==================================================
	/**
	 * 	单个上传图片
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/upload")
	public String uploadFile(@RequestParam("file")MultipartFile file) throws IOException { 
	   String fileExtName = file.getOriginalFilename().split("\\.")[1];
	   String file_Name = file.getOriginalFilename().split("\\.")[0];
	   LOGGER.info("上传文件=======================");  
       byte[] fileBuff = file.getBytes();  
       String[] files = null;  
       // 建立连接  
       TrackerClient tracker = new TrackerClient();  
       TrackerServer trackerServer = tracker.getConnection();  
       StorageServer storageServer = null;  
       StorageClient client = new StorageClient(trackerServer, storageServer);  
 
       // 设置元信息  
       NameValuePair[] metaList = new NameValuePair[3];  
       metaList[0] = new NameValuePair("fileName", file_Name);  
       metaList[1] = new NameValuePair("fileExtName", fileExtName);  
       metaList[2] = new NameValuePair("fileLength", String.valueOf(fileBuff.length));  
 
       // 上传文件  
       try {  
           files = client.upload_file(fileBuff, fileExtName, null);  
       } catch (Exception e) {  
    	   LOGGER.info("Upload file \"" + file_Name + "\"fails");  
       }  
       trackerServer.close();  
       LOGGER.info(Arrays.asList(files).toString());  
       return "success";  
   } 
	
	/**
	 * 	imagelist图片上传接口
	 * 
	 * @param file
	 * @param serialNo
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/pass/upload")
	public Resp uploadPass(@RequestParam("file")MultipartFile file,String serialNo) throws IOException { 
	   LOGGER.info("上传文件=======================");  
       byte[] fileBuff = file.getBytes();
       final String topic = config.getImageImageListTopic();
       LOGGER.info("serialNo:"+serialNo);
	   boolean succeed = this.imageWeaveInManager.imageIn(topic, serialNo, fileBuff);
	   Resp resp = (succeed) ? Resp.OK : Resp.FAILURE.withPhase("Failed to sync data");
	   return resp;
   }  	
	
}
