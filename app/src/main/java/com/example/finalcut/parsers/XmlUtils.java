package com.example.finalcut.parsers;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public final class XmlUtils {

    public static String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n";

    private XmlUtils() {
    }

    public static String cutTag(String source, String cutTag, StringBuilder builderBefore, StringBuilder builderAfter) {
        int indexOf = source.indexOf(cutTag);
        if (indexOf == -1) {
            throw new RuntimeException("Not  Found");
        }
        String strBefore = source.substring(0, indexOf - 1);
        builderBefore.append(strBefore);
        source = source.substring(indexOf + cutTag.length(), source.length());
        indexOf = source.indexOf(cutTag);
        String target = source.substring(1, indexOf - 2);
        String strAfter = source.substring(indexOf + cutTag.length() + 1, source.length());
        builderAfter.append(strAfter);
        return target;
    }

    public static Map<String, String> parseResponce(String input) throws ParserConfigurationException, SAXException, IOException {
        Map<String, String> result = new HashMap<String, String>();
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        StringReader reader = new StringReader(input);
        parser.parse(new InputSource(reader), new RegularStructureHandler(result));
        reader.close();
        return result;
    }
}