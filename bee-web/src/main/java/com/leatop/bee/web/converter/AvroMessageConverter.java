/**
 * File: AvroMessageConverter.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.converter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.avro.data.RecordBuilder;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.leatop.bee.common.domain.ETCPassList;
import com.leatop.bee.common.domain.ETCTradeList;
import com.leatop.bee.common.domain.FlagCarPassList;
import com.leatop.bee.common.domain.FlagCpcPassList;
import com.leatop.bee.common.domain.FlagEtcPassList;
import com.leatop.bee.common.domain.FlagRunStatus;
import com.leatop.bee.common.domain.FlagTollList;
import com.leatop.bee.common.domain.ImageList;
import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.data.converter.Converter;
import com.leatop.bee.web.po.ETCPassListAvro;
import com.leatop.bee.web.po.ETCTradeListAvro;
import com.leatop.bee.web.po.FlagCarPassListAvro;
import com.leatop.bee.web.po.FlagCpcPassListAvro;
import com.leatop.bee.web.po.FlagEtcPassListAvro;
import com.leatop.bee.web.po.FlagRunStatusAvro;
import com.leatop.bee.web.po.FlagTollListAvro;
import com.leatop.bee.web.po.ImageListAvro;

/**
 * @author Dorsey
 *
 */
public class AvroMessageConverter implements Converter<SpecificRecord> {

	private static final Logger LOG = Logger.getLogger(AvroMessageConverter.class.getName());
	private static final Map<Class<? extends TrafficData<?>>, RecordBuilder<? extends SpecificRecord>> CONVERTER = new HashMap<>();
	private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap<>();
	private static final Object lock = new Object();

	static {
		CONVERTER.put(ETCTradeList.class, ETCTradeListAvro.newBuilder());
		CONVERTER.put(ETCPassList.class, ETCPassListAvro.newBuilder());
		CONVERTER.put(ImageList.class, ImageListAvro.newBuilder());

		/**
		 * @since 0.0.2-SNAPSHOT
		 */
		CONVERTER.put(FlagTollList.class, FlagTollListAvro.newBuilder());
		CONVERTER.put(FlagCpcPassList.class, FlagCpcPassListAvro.newBuilder());
		CONVERTER.put(FlagCarPassList.class, FlagCarPassListAvro.newBuilder());
		CONVERTER.put(FlagEtcPassList.class, FlagEtcPassListAvro.newBuilder());
		CONVERTER.put(FlagRunStatus.class, FlagRunStatusAvro.newBuilder());

		// avro wrapper classes for java primitive types
		WRAPPERS.put(String.class, CharSequence.class);
		WRAPPERS.put(byte[].class, ByteBuffer.class);
		WRAPPERS.put(Date.class, CharSequence.class);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public <ID> SpecificRecord convertFrom(final TrafficData<ID> data) {
		final Class<? extends TrafficData> clazz = data.getClass();
		SpecificRecord value = null;
		synchronized (lock) {
			RecordBuilder<? extends SpecificRecord> builder = CONVERTER.get(clazz);
			if (builder != null) {
				copyData(data, builder);
			}
			value = builder.build();
		}
		return value;
	}

	private static <ID> void copyData(final TrafficData<ID> data,
			final RecordBuilder<? extends SpecificRecord> builder) {
		ReflectionUtils.doWithFields(data.getClass(), new FieldCallback() {

			@Override
			public void doWith(final Field field)
					throws IllegalArgumentException, IllegalAccessException {
				final String name = field.getName();
				final String methodName = "set" + name.substring(0, 1).toUpperCase()
						+ name.substring(1);
				try {
					field.setAccessible(true);
					Object value = field.get(data);
					if (value == null) {
						// short circuit.
						return;
					}

					Method method = ReflectionUtils.findMethod(builder.getClass(), methodName,
							field.getType());
					method = (method == null) ? ReflectionUtils.findMethod(builder.getClass(),
							methodName, wrapperClassOf(field.getType())) : method;
					if (method == null) {
						LOG.warning("No such method [" + methodName + "] found in builder: "
								+ builder.getClass());
						return;
					}

					method.setAccessible(true);
					if (field.getType() == Date.class && value != null) {
						DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String val = format.format((Date) value);
						method.invoke(builder, new Object[] { val });
						return;
					}

					method.invoke(builder, value);
				} catch (SecurityException e) {
					LOG.warning("Security violation detected on accessing field: [ " + name + "], "
							+ builder.getClass().getName());
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new IllegalArgumentException(e);
				}
			}
		});
	}

	private static Class<?> wrapperClassOf(final Class<?> original) {
		return WRAPPERS.get(original);
	}
}
