/**
 * File: BeeWebConfiguration.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dorsey
 *
 */
@Configuration
@ConfigurationProperties(prefix = "bee.web")
public class BeeWebConfiguration {

	@Deprecated
	private String etcTradeListTopic;
	@Deprecated
	private String etcPassListTopic;
	@Deprecated
	private String imageListTopic;

	private String flagTollListTopic;
	private String flagCarPassListTopic;
	private String flagCpcPassListTopic;
	private String flagEtcPassListTopic;
	private String flagRunStatusTopic;
	private String errorStoragePath;

	private String imageEtcPassListTopic;

	private String imageImageListTopic;

	// for report purpose
	private String reporterUrl;

	/**
	 * Empty constructor of {@link BeeWebConfiguration}.
	 */
	public BeeWebConfiguration() {
		// empty for initialization.
	}

	/**
	 * @return the etcTradeListTopic
	 */
	public String getEtcTradeListTopic() {
		return etcTradeListTopic;
	}

	/**
	 * @param etcTradeListTopic
	 *            the etcTradeListTopic to set
	 */
	public void setEtcTradeListTopic(final String etcTradeListTopic) {
		this.etcTradeListTopic = etcTradeListTopic;
	}

	/**
	 * @return the etcPassListTopic
	 */
	public String getEtcPassListTopic() {
		return etcPassListTopic;
	}

	/**
	 * @param etcPassListTopic
	 *            the etcPassListTopic to set
	 */
	public void setEtcPassListTopic(final String etcPassListTopic) {
		this.etcPassListTopic = etcPassListTopic;
	}

	/**
	 * @return the imageListTopic
	 */
	public String getImageListTopic() {
		return imageListTopic;
	}

	/**
	 * @param imageListTopic
	 *            the imageListTopic to set
	 */
	public void setImageListTopic(final String imageListTopic) {
		this.imageListTopic = imageListTopic;
	}

	/**
	 * @return the flagTollListTopic
	 */
	public String getFlagTollListTopic() {
		return flagTollListTopic;
	}

	/**
	 * @param flagTollListTopic
	 *            the flagTollListTopic to set
	 */
	public void setFlagTollListTopic(final String flagTollListTopic) {
		this.flagTollListTopic = flagTollListTopic;
	}

	/**
	 * @return the flagCarPassListTopic
	 */
	public String getFlagCarPassListTopic() {
		return flagCarPassListTopic;
	}

	/**
	 * @param flagCarPassListTopic
	 *            the flagCarPassListTopic to set
	 */
	public void setFlagCarPassListTopic(final String flagCarPassListTopic) {
		this.flagCarPassListTopic = flagCarPassListTopic;
	}

	/**
	 * @return the flagCpcPassListTopic
	 */
	public String getFlagCpcPassListTopic() {
		return flagCpcPassListTopic;
	}

	/**
	 * @param flagCpcPassListTopic
	 *            the flagCpcPassListTopic to set
	 */
	public void setFlagCpcPassListTopic(final String flagCpcPassListTopic) {
		this.flagCpcPassListTopic = flagCpcPassListTopic;
	}

	/**
	 * @return the flagEtcPassListTopic
	 */
	public String getFlagEtcPassListTopic() {
		return flagEtcPassListTopic;
	}

	/**
	 * @param flagEtcPassListTopic
	 *            the flagEtcPassListTopic to set
	 */
	public void setFlagEtcPassListTopic(final String flagEtcPassListTopic) {
		this.flagEtcPassListTopic = flagEtcPassListTopic;
	}

	/**
	 * @return the flagRunStatusTopic
	 */
	public String getFlagRunStatusTopic() {
		return flagRunStatusTopic;
	}

	/**
	 * @param flagRunStatusTopic
	 *            the flagRunStatusTopic to set
	 */
	public void setFlagRunStatusTopic(final String flagRunStatusTopic) {
		this.flagRunStatusTopic = flagRunStatusTopic;
	}

	/**
	 * @return the errorStoragePath
	 */
	public String getErrorStoragePath() {
		return errorStoragePath;
	}

	/**
	 * @param errorStoragePath
	 *            the errorStoragePath to set
	 */
	public void setErrorStoragePath(final String errorStoragePath) {
		this.errorStoragePath = errorStoragePath;
	}

	public String getImageEtcPassListTopic() {
		return imageEtcPassListTopic;
	}

	public void setImageEtcPassListTopic(final String imageEtcPassListTopic) {
		this.imageEtcPassListTopic = imageEtcPassListTopic;
	}

	public String getImageImageListTopic() {
		return imageImageListTopic;
	}

	public void setImageImageListTopic(final String imageImageListTopic) {
		this.imageImageListTopic = imageImageListTopic;
	}

	/**
	 * @return the reporterUrl
	 */
	public String getReporterUrl() {
		return reporterUrl;
	}

	/**
	 * @param reporterUrl
	 *            the reporterUrl to set
	 */
	public void setReporterUrl(final String reporterUrl) {
		this.reporterUrl = reporterUrl;
	}

}
