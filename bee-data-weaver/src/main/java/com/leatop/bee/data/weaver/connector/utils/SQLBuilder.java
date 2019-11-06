/**
 * File: SQLBuilder.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月9日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.utils;

import java.util.Iterator;

import com.leatop.bee.data.weaver.connector.jdbc.domain.ColumnId;
import com.leatop.bee.data.weaver.connector.jdbc.domain.TableId;

/**
 * @author Dorsey
 *
 */
public class SQLBuilder {

	protected static final QuoteMethod DEFAULT_QUOTE_METHOD = QuoteMethod.ALWAYS;
	private final Identifier identifier;
	private final StringBuilder builder = new StringBuilder();
	private QuoteMethod quoteSqlIdentifiers = DEFAULT_QUOTE_METHOD;

	public SQLBuilder() {
		this(null);
	}

	public SQLBuilder(final Identifier identifier) {
		this.identifier = (identifier != null) ? identifier : Identifier.DEFAULT_IDENTIFIER;
	}

	// ~~~ methods
	// =========================================================================================
	public static Transform<String> quote() {
		return new Transform<String>() {

			@Override
			public void apply(final SQLBuilder builder, final String input) {
				builder.appendColumnName(input);
			}
		};
	}

	public static Transform<ColumnId> columnNames() {
		return new Transform<ColumnId>() {

			@Override
			public void apply(final SQLBuilder builder, final ColumnId input) {
				builder.appendColumnName(input.getColumnName());
			}
		};
	}

	public static Transform<ColumnId> columnNamesWith(final String appended) {
		return new Transform<ColumnId>() {

			@Override
			public void apply(final SQLBuilder builder, final ColumnId input) {
				builder.appendColumnName(input.getColumnName());
				builder.appendColumnName(appended);
			}
		};
	}

	public static Transform<ColumnId> prefixColumnNameWith(final String prefix) {
		return new Transform<ColumnId>() {

			@Override
			public void apply(final SQLBuilder builder, final ColumnId input) {
				builder.appendColumnName(prefix);
				builder.appendColumnName(input.getColumnName());
			}
		};
	}

	public SQLBuilder appendColumnName(final String name) {
		return appendColumnName(name, quoteSqlIdentifiers);
	}

	public SQLBuilder appendColumnName(final String name, final QuoteMethod quoteMethod) {
		appendLeadingQuote(quoteMethod);
		builder.append(name);
		appendTrailQuote(quoteMethod);

		return this;
	}

	protected SQLBuilder appendTrailQuote(final QuoteMethod quoteMethod) {
		switch (quoteMethod) {
		case ALWAYS:
			builder.append(identifier.getTrailQuote());
			break;

		default:
			break;
		}

		return this;
	}

	public SQLBuilder appendIdentifierDelim() {
		builder.append(this.identifier.getDelim());
		return this;
	}

	protected SQLBuilder appendLeadingQuote(final QuoteMethod quoteMethod) {
		switch (quoteMethod) {
		case ALWAYS:
			builder.append(identifier.getLeadingQuote());
			break;

		default:
			break;
		}

		return this;
	}

	public SQLBuilder appendIdentifier(final String name) {
		return appendIdentifier(name, quoteSqlIdentifiers);
	}

	public SQLBuilder appendIdentifier(final String name, final boolean useQuote) {
		return appendIdentifier(name, (useQuote) ? QuoteMethod.ALWAYS : QuoteMethod.NEVER);
	}

	public SQLBuilder appendIdentifier(final String name, final QuoteMethod quoteMethod) {
		appendLeadingQuote(quoteMethod);
		builder.append(name);
		appendTrailQuote(quoteMethod);

		return this;
	}

	public SQLBuilder appendTableName(final String tableName) {
		return appendTableName(tableName, quoteSqlIdentifiers);
	}

	public SQLBuilder appendTableName(final String tableName, final QuoteMethod quoteMethod) {
		appendLeadingQuote(quoteMethod);
		builder.append(tableName);
		appendTrailQuote(quoteMethod);

		return this;
	}

	public static SQLBuilder create() {
		return new SQLBuilder(null);
	}

	public SQLBuilder append(final Object obj) {
		return append(obj, quoteSqlIdentifiers);
	}

	public SQLBuilder append(final Object obj, final boolean useQuote) {
		return append(obj, (useQuote) ? QuoteMethod.ALWAYS : QuoteMethod.NEVER);
	}

	public SQLBuilder append(final Object obj, final QuoteMethod quoteMethod) {
		if ((obj instanceof SQLize)) {
			((SQLize) obj).appendTo(this, quoteMethod);
		} else if (obj != null) {
			builder.append(obj);
		}

		return this;
	}

	public <T> SQLBuilder append(final T elem, final Transform<T> transform) {
		if (transform != null) {
			transform.apply(this, elem);
		} else {
			append(elem);
		}

		return this;
	}

	public SQLBuilder insertInto(final TableId tableId) {
		this.builder.append(Constants.INSERT_INTO).append(Constants.SPACE);
		append(tableId);
		this.builder.append(Constants.SPACE);

		return this;
	}

	public ListBuilder<Object> listBuilder() {
		return new BasicListBuilder<Object>();
	}

	public SQLBuilder appendMultiple(final String delim, final String identifier, final int times) {
		for (int index = 0; index < times; index++) {
			if (index > 0) {
				append(delim);
			}

			append(identifier);
		}

		return this;
	}

	@Override
	public String toString() {
		return builder.toString();
	}

	// ~~~ enclosing class
	// =========================================================================================
	public static interface SQLize {

		void appendTo(final SQLBuilder builder, final boolean useQuotes);

		default void appendTo(final SQLBuilder builder, final QuoteMethod quoteMethod) {
			switch (quoteMethod) {
			case ALWAYS:
				appendTo(builder, true);
				break;

			default:
				break;
			}
		}
	}

	public static interface Transform<T> {

		void apply(final SQLBuilder builder, final T input);
	}

	public static interface Constants {

		final String INSERT_INTO = "INSERT INTO";
		final String SPACE = " ";
		final String COMMA_WITH_SPACE_DELIMITER = ", ";
		final String COMMA_DELIMITER = ",";
		final String QUESTION_MARK_DELIMITER = "?";
		final String VALUES = "VALUES ";
		final String LEFT_PARENTHESE = "(";
		final String RIGHT_PARENTHESE = ")";
	}

	public static interface ListBuilder<T> {

		/**
		 * Specify the delimiter to delimited the identifiers.
		 */
		ListBuilder<T> delimitedBy(final String delimiter);

		<R> ListBuilder<R> transformedBy(final Transform<R> transform);

		SQLBuilder of(Iterable<? extends T> objs);

		default SQLBuilder of(final Iterable<? extends T> objs1,
				final Iterable<? extends T> objs2) {
			of(objs1);
			return of(objs2);
		}

		default SQLBuilder of(final Iterable<? extends T> objs1, final Iterable<? extends T> objs2,
				final Iterable<? extends T> objs3) {
			of(objs1);
			of(objs2);
			return of(objs3);
		}
	}

	protected class BasicListBuilder<T> implements ListBuilder<T> {

		private final String delimilter;
		private final Transform<T> transform;

		public BasicListBuilder() {
			this(Constants.COMMA_WITH_SPACE_DELIMITER, null);
		}

		public BasicListBuilder(final String delimilter, final Transform<T> transform) {
			this.delimilter = delimilter;
			this.transform = transform;
		}

		@Override
		public ListBuilder<T> delimitedBy(final String delimiter) {
			return new BasicListBuilder<T>(delimiter, transform);
		}

		@Override
		public <R> ListBuilder<R> transformedBy(final Transform<R> transform) {
			return new BasicListBuilder<R>(delimilter, transform);
		}

		@Override
		public SQLBuilder of(final Iterable<? extends T> objs) {
			Iterator<? extends T> iter = objs.iterator();
			while (iter.hasNext()) {
				T elem = iter.next();
				append(elem, transform);

				if (iter.hasNext()) {
					append(delimilter);
				}
			}

			return SQLBuilder.this;
		}

	}

	// ~~~ settters
	// ==================================================================================
	public SQLBuilder setQuoteSqlIdentifiers(final QuoteMethod quoteSqlIdentifiers) {
		this.quoteSqlIdentifiers = quoteSqlIdentifiers;
		return this;
	}

}
