/**
 * File: OffsetProtocols.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月12日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.utils.QuoteMethod;
import com.leatop.bee.data.weaver.connector.utils.SQLBuilder;

/**
 * @description
 */
public class OffsetProtocols {

	/**
	 * @description offset
	 * @param tableId
	 * @return
	 */
	public static Map<String, String> sourcePartitionForProtocolV1(final TableId tableId) {
		String fqn = SQLBuilder.create().append(tableId, QuoteMethod.NEVER).toString();
		Map<String, String> partitionForV1 = new HashMap<>();
		partitionForV1.put(JDBCSourceConnectorConstants.TABLE_NAME_KEY, fqn);
		partitionForV1.put(JDBCSourceConnectorConstants.OFFSET_PROTOCOL_VERSION_KEY,
				JDBCSourceConnectorConstants.PROTOCOL_VERSION_ONE);
		return partitionForV1;
	}

	/**
	 * @description
	 * @param tableId
	 * @return
	 */
	public static Map<String, String> sourcePartitionForProtocolV0(final TableId tableId) {
		return Collections.singletonMap(JDBCSourceConnectorConstants.TABLE_NAME_KEY,
				tableId.getTableName());
	}
}
