/**
 * File: AvroSchemaUtils.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.serializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;

/**
 * @author Dorsey
 *
 */
public class AvroSchemaUtils {

	private static final Map<String, Schema> PRIMITIVE_SCHEMAS;

	static {
		Schema.Parser parser = new Schema.Parser();
		PRIMITIVE_SCHEMAS = new HashMap<>();
		PRIMITIVE_SCHEMAS.put("Null", createPrimitiveSchema(parser, "null"));
		PRIMITIVE_SCHEMAS.put("Boolean", createPrimitiveSchema(parser, "boolean"));
		PRIMITIVE_SCHEMAS.put("Integer", createPrimitiveSchema(parser, "int"));
		PRIMITIVE_SCHEMAS.put("Long", createPrimitiveSchema(parser, "long"));
		PRIMITIVE_SCHEMAS.put("Float", createPrimitiveSchema(parser, "float"));
		PRIMITIVE_SCHEMAS.put("Double", createPrimitiveSchema(parser, "double"));
		PRIMITIVE_SCHEMAS.put("String", createPrimitiveSchema(parser, "string"));
		PRIMITIVE_SCHEMAS.put("Bytes", createPrimitiveSchema(parser, "bytes"));
	}

	private static Schema createPrimitiveSchema(final Schema.Parser parser, final String type) {
		String schemaString = String.format("{\"type\" : \"%s\"}", type);
		return parser.parse(schemaString);
	}

	protected static Schema copyOf(final Schema schema) {
		return new Schema.Parser().parse(schema.toString());
	}

	protected static Map<String, Schema> getPrimitiveSchemas() {
		return Collections.unmodifiableMap(PRIMITIVE_SCHEMAS);
	}

	public static Schema getSchema(final Object object) {
		if (object == null) {
			return PRIMITIVE_SCHEMAS.get("Null");
		} else if (object instanceof Boolean) {
			return PRIMITIVE_SCHEMAS.get("Boolean");
		} else if (object instanceof Integer) {
			return PRIMITIVE_SCHEMAS.get("Integer");
		} else if (object instanceof Long) {
			return PRIMITIVE_SCHEMAS.get("Long");
		} else if (object instanceof Float) {
			return PRIMITIVE_SCHEMAS.get("Float");
		} else if (object instanceof Double) {
			return PRIMITIVE_SCHEMAS.get("Double");
		} else if (object instanceof CharSequence) {
			return PRIMITIVE_SCHEMAS.get("String");
		} else if (object instanceof byte[]) {
			return PRIMITIVE_SCHEMAS.get("Bytes");
		} else if (object instanceof GenericContainer) {
			return ((GenericContainer) object).getSchema();
		} else {
			throw new IllegalArgumentException(
					"Unsupported Avro type. Supported types are null, Boolean, Integer, Long, "
							+ "Float, Double, String, byte[] and IndexedRecord");
		}
	}
}
