/**
 * File: FileUtils.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月29日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.utils;

import java.io.File;

/**
 * A utility class which provides the common operations on <code>file</code>.
 * 
 * @author Dorsey
 *
 */
public class FileUtils {

	public static void mkdirIfAbsent(final String path) {
		if (path != null && path.isEmpty() == false) {
			File dir = new File(path);

			if (!dir.exists()) {
				dir.mkdirs();
				return;
			}

			if (!dir.isDirectory()) {
				throw new IllegalArgumentException(
						"path: " + path + " specified is not a directory");
			}
		}
	}
}
