package com.glaf.core.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.glaf.core.base.Template;
import com.glaf.core.util.Tools;

public class TemplateReader {

	public Element getRootElement(InputStream inputStream) {
		SAXReader xmlReader = new SAXReader();
		Document doc = null;
		try {
			doc = xmlReader.read(inputStream);
		} catch (DocumentException ex) {
			throw new RuntimeException(ex);
		}
		Element root = doc.getRootElement();
		return root;
	}

	@SuppressWarnings("unchecked")
	public List<Template> readTemplates(InputStream inputStream) {
		List<Template> templates = new ArrayList<Template>();
		Element root = this.getRootElement(inputStream);
		List<Element> elements = root.elements("template");
		if (elements != null && !elements.isEmpty()) {
			for (Element element : elements) {
				Template tpl = new Template();
				tpl.setName(element.attributeValue("name"));
				tpl.setDesccription(element.attributeValue("desccription"));
				List<Element> elems = element.elements();
				if (elems != null && !elems.isEmpty()) {
					Map<String, Object> dataMap = new HashMap<String, Object>();
					for (Element em : elems) {
						dataMap.put(em.getName(), em.getTextTrim());
					}
					Tools.populate(tpl, dataMap);
				}
				templates.add(tpl);
			}
		}

		return templates;
	}

}
