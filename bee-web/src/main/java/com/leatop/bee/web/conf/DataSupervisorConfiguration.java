package com.leatop.bee.web.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "bee.data.supervisor")
@PropertySource(encoding = "UTF-8", value = "classpath:bee-data-supervisor.properties")
public class DataSupervisorConfiguration {

	private String preProcessors = null;
	private String postProcessors = null;

	/**
	 * Empty constructor of {@link DataSupervisorConfiguration}.
	 */
	public DataSupervisorConfiguration() {
		super();
	}

	/**
	 * @return the preProcessors
	 */
	public String getPreProcessors() {
		return preProcessors;
	}

	/**
	 * @param preProcessors
	 *            the preProcessors to set
	 */
	public void setPreProcessors(final String preProcessors) {
		this.preProcessors = preProcessors;
	}

	/**
	 * @return the postProcessors
	 */
	public String getPostProcessors() {
		return postProcessors;
	}

	/**
	 * @param postProcessors
	 *            the postProcessors to set
	 */
	public void setPostProcessors(final String postProcessors) {
		this.postProcessors = postProcessors;
	}

	@Override
	public String toString() {
		return "Supervisors: {preProcessors: " + getPreProcessors() + ", postProcessors: " + getPostProcessors() + "}";
	}
}
