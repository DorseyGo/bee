/**
 * File: SchemaPO.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.po;

import java.io.Serializable;
import java.util.Objects;

/**
 * 对象模型管理实体类
 * 
 * @author Dorsey
 *
 */
public class SchemaEntity implements Serializable {

	private static final long serialVersionUID = -5179880441858351469L;

	private Integer id;
	private int version;//版本号
	private String subject;//对象类
	private String schema;//转换对象

	/**
	 * Empty constructor of {@link SchemaEntity}.
	 */
	public SchemaEntity() {
		this.version = 0;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(final int version) {
		this.version = version;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(final String schema) {
		this.schema = schema;
	}

	@Override
	public String toString() {
		return "SchemaPO [id=" + id + ", version=" + version + ", subject=" + subject + ", schema="
				+ schema + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, version, subject, schema);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj == null || !(obj instanceof SchemaEntity))
			return false;

		SchemaEntity other = (SchemaEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(version, other.version)
				&& Objects.equals(subject, other.subject) && Objects.equals(schema, other.schema);
	}

}
