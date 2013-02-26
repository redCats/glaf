/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.dts.parse;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;

public class XmlMappingReader {

	public TableModel read(java.io.InputStream inputStream) {
		TableModel classDefinition = new TableModel();
		SAXReader xmlReader = new SAXReader();
		try {
			Document doc = xmlReader.read(inputStream);
			Element root = doc.getRootElement();
			Element element = root.element("entity");
			if (element != null) {
				classDefinition.setEntityName(element.attributeValue("name"));
				classDefinition.setPrimaryKey(element
						.attributeValue("primaryKey"));
				classDefinition.setTableName(element.attributeValue("table"));
				classDefinition.setTitle(element.attributeValue("title"));
				classDefinition.setStopWord(element.attributeValue("stopWord"));
				classDefinition.setPackageName(element
						.attributeValue("package"));
				classDefinition.setEnglishTitle(element
						.attributeValue("englishTitle"));
				classDefinition.setFilePrefix(element
						.attributeValue("filePrefix"));
				classDefinition.setParseType(element
						.attributeValue("parseType"));
				classDefinition.setParseClass(element
						.attributeValue("parseClass"));
				classDefinition.setAggregationKey(element
						.attributeValue("aggregationKeys"));

				String startRow = element.attributeValue("startRow");
				if (StringUtils.isNumeric(startRow)) {
					classDefinition.setStartRow(Integer.parseInt(startRow));
				}
				String stopSkipRow = element.attributeValue("stopSkipRow");
				if (StringUtils.isNumeric(stopSkipRow)) {
					classDefinition.setStopSkipRow(Integer
							.parseInt(stopSkipRow));
				}

				String minLength = element.attributeValue("minLength");
				if (StringUtils.isNumeric(minLength)) {
					classDefinition.setMinLength(Integer.parseInt(minLength));
				}

				String batchSize = element.attributeValue("batchSize");
				if (StringUtils.isNumeric(batchSize)) {
					classDefinition.setBatchSize(Integer.parseInt(batchSize));
				}

				List<?> rows = element.elements("property");
				if (rows != null && rows.size() > 0) {
					Iterator<?> iterator = rows.iterator();
					while (iterator.hasNext()) {
						Element elem = (Element) iterator.next();
						ColumnModel field = new ColumnModel();
						this.readField(elem, field);
						classDefinition.addColumn(field);
						if (StringUtils.equals(classDefinition.getPrimaryKey(),
								field.getColumnName())) {
							classDefinition.setIdColumn(field);
						}
					}
				}
			}

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return classDefinition;
	}

	protected void readField(Element elem, ColumnModel field) {
		field.setName(elem.attributeValue("name"));
		field.setType(elem.attributeValue("type"));
		field.setColumnName(elem.attributeValue("column"));
		field.setTitle(elem.attributeValue("title"));
		field.setSecondTitle(elem.attributeValue("secondTitle"));
		field.setTrimType(elem.attributeValue("trimType"));
		field.setValueExpression(elem.attributeValue("valueExpression"));
		field.setFormat(elem.attributeValue("format"));
		field.setCurrency(elem.attributeValue("currency"));

		/**
		 * 如果是占位符，则不存储该字段
		 */
		if ("true".equals(elem.attributeValue("temporary"))) {
			field.setTemporary(true);
		}

		String length = elem.attributeValue("length");
		if (StringUtils.isNumeric(length)) {
			field.setLength(Integer.parseInt(length));
		}

		String precision = elem.attributeValue("precision");
		if (StringUtils.isNumeric(precision)) {
			field.setPrecision(Integer.parseInt(precision));
		}

		String decimal = elem.attributeValue("decimal");
		if (StringUtils.isNumeric(decimal)) {
			field.setDecimal(Integer.parseInt(decimal));
		}

		String position = elem.attributeValue("position");
		if (StringUtils.isNumeric(position)) {
			field.setPosition(Integer.parseInt(position));
		}
	}
}