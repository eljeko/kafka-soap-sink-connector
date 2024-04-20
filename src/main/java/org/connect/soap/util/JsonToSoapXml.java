package org.connect.soap.util;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import java.io.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.HashMap;

public class JsonToSoapXml {

    private final Transformer transformer;

    public JsonToSoapXml(String xsltFilePath) throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File(xsltFilePath));
        this.transformer = factory.newTransformer(xslt);
    }

    public String toXml(String json) {
        JSONObject jsonObject = new JSONObject(json);
        return XML.toString(jsonObject);
    }

    public String toXml(HashMap json) {
        JSONObject jsonObject = new JSONObject(json);
        return XML.toString(jsonObject);
    }

    public String tosSoap(String json) throws TransformerException {
        JSONObject jsonObject = new JSONObject(json);
        return getSoapString(jsonObject);
    }

    public String tosSoap(HashMap json) throws TransformerException {
        JSONObject jsonObject = new JSONObject(json);
        return getSoapString(jsonObject);
    }

    private String getSoapString(JSONObject jsonObject) throws TransformerException {
        String xml = XML.toString(jsonObject);
        Source text = new StreamSource(new StringReader(xml));
        StringWriter stringWriter = new StringWriter();
        transformer.transform(text, new StreamResult(stringWriter));
        return stringWriter.getBuffer().toString();
    }

    public Document xmlStringToDocument(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));

        Document doc = db.parse(is);
        return doc;
    }


}

