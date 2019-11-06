/**
 * File: ListJsonDeserializer.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.serializer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leatop.bee.common.domain.FlagCarPassList;
import com.leatop.bee.common.domain.FlagCpcPassList;
import com.leatop.bee.common.domain.FlagEtcPassList;
import com.leatop.bee.common.domain.FlagRunStatus;
import com.leatop.bee.common.domain.FlagTollList;
import com.leatop.bee.common.domain.TrafficData;

/**
 * json对象反序列化
 * 
 * @author Dorsey
 *
 */
@Component
public class ListJsonDeserializer {

	private final ObjectMapper mapper = new ObjectMapper();
	private static final Map<String, Class<?>> LIST_DOMAINS = new HashMap<>();

	static {
		LIST_DOMAINS.put(FlagTollList.class.getSimpleName(), FlagTollList.class);
		LIST_DOMAINS.put(FlagEtcPassList.class.getSimpleName(), FlagEtcPassList.class);
		LIST_DOMAINS.put(FlagCpcPassList.class.getSimpleName(), FlagCpcPassList.class);
		LIST_DOMAINS.put(FlagCarPassList.class.getSimpleName(), FlagCarPassList.class);
		LIST_DOMAINS.put(FlagRunStatus.class.getSimpleName(), FlagRunStatus.class);
	}

	public ListJsonDeserializer() {
		// empty for initialization.
	}

	public <ID> List<TrafficData<ID>> deserialize(final String listName, final InputStream is)
			throws DeserializationException {
		List<TrafficData<ID>> trafficData = null;
		try {
			trafficData = mapper.readValue(is,
					getCollectionType(this.mapper, List.class, LIST_DOMAINS.get(listName)));
		} catch (Exception e) {
			throw new DeserializationException(e);
		}

		return trafficData;
	}

	public static JavaType getCollectionType(final ObjectMapper mapper,
			final Class<?> collectionClass, final Class<?>... elemClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elemClasses);
	}

	/**
	 * An exception is thrown when any error detected during de-serialize.
	 * 
	 * @author Dorsey
	 *
	 */
	public static class DeserializationException extends Exception {

		private static final long serialVersionUID = -721387352070324129L;

		public DeserializationException() {
			super();
		}

		public DeserializationException(final String message) {
			super(message);
		}

		public DeserializationException(final Throwable cause) {
			super(cause);
		}

		public DeserializationException(final String message, final Throwable cause) {
			super(message, cause);
		}
	}
}
