package com.example.finalcut.parsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Map;

public class RegularStructureHandler extends DefaultHandler {

    private final Map<String, String> resultMap;
    private String item;

    public RegularStructureHandler(Map<String, String> resultMap) {
        this.resultMap = resultMap;
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if (item != null) {
            if (length > 0) {
                resultMap.put(item, new String(ch, start, length).trim());
            }
        }
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        item = qName;
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        item = null;
    }
}