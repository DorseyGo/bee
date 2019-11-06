/**
 * File: FieldSplitter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年8月13日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.data.weaver.connector.jdbc.source.domain;

import java.util.List;

import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;
import com.leatop.bee.data.weaver.connector.jdbc.source.processor.FieldTransformer;

/**
 * @author DORSEy
 *
 */
public class FieldTransformerInfo {
	private final TableId tableId;
	private final String srcFieldName;
	private final List<SubFieldTransformer> children;

	/**
	 * Constructor of {@link FieldTransformerInfo}.
	 * 
	 */
	public FieldTransformerInfo(final TableId tableId, final String srcFieldName,
			final List<SubFieldTransformer> children) {
		this.tableId = tableId;
		this.srcFieldName = srcFieldName;
		this.children = children;
	}

	/**
	 * @return the tableId
	 */
	public TableId getTableId() {
		return tableId;
	}

	/**
	 * @return the srcFieldName
	 */
	public String getSrcFieldName() {
		return srcFieldName;
	}

	/**
	 * @return the children
	 */
	public List<SubFieldTransformer> getChildren() {
		return children;
	}

	public static class SubFieldTransformer {
		private final String derivedFieldName;
		private final int sqlType;
		private final FieldTransformer processor;

		/**
		 * @param derivedFieldName
		 * @param processor
		 */
		public SubFieldTransformer(final String derivedFieldName, final FieldTransformer processor) {
			this(derivedFieldName, processor.getSqlType(), processor);
		}

		/**
		 * @param derivedFieldName
		 * @param sqlType
		 * @param processor
		 */
		public SubFieldTransformer(final String derivedFieldName, final int sqlType,
				final FieldTransformer processor) {
			this.derivedFieldName = derivedFieldName;
			this.sqlType = sqlType;
			this.processor = processor;
		}

		/**
		 * @return the derivedFieldName
		 */
		public String getDerivedFieldName() {
			return derivedFieldName;
		}

		/**
		 * @return the sqlType
		 */
		public int getSqlType() {
			return sqlType;
		}

		/**
		 * @return the processor
		 */
		public FieldTransformer getProcessor() {
			return processor;
		}

	}
}
