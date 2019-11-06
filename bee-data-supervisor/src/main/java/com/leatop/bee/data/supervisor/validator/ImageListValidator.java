/**
 * File: ImageListValidator.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.validator;


import com.leatop.bee.common.utils.ElasticSearchUtils;
import com.leatop.bee.data.supervisor.introspector.DuplicationalIntrospector.DuplicationalValidator;

/**
 * @author Dorsey
 *
 */
public class ImageListValidator implements DuplicationalValidator<String> {

	
	private static final String indexName = "po_03";
	
	private static final String esType = "imageList";
	
	private static final String field = "serialNo.keyword";
	
	public boolean validate(final String id) {
		return ElasticSearchUtils.getInstance().query(indexName, esType, field, id);
	}

}
