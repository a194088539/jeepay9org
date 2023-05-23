package org.jeepay.common.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.jeepay.common.bean.result.PayException;
import org.jeepay.common.exception.PayErrorException;
import org.jeepay.common.util.str.StringUtils;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * XML工具
 *
 * @author egan
 *         <pre>
 *         email egzosn@gmail.com
 *         date 2016-6-2 19:45:06
 *         </pre>
 */
public class XML {


    /**
     * 解析xml并转化为json值
     *
     * @param in 输入流
     * @return Json值
     */
    public static JSONObject toJSONObject(InputStream in) {

        if (null == in) {
            return null;
        }

        try {
            return (JSONObject) inputStream2Map(in, null);
        } catch (IOException e) {
            throw new PayErrorException(new PayException("IOException", e.getMessage()));
        }


    }

    /**
     * 解析xml并转化为Json值
     *
     * @param content json字符串
     * @return Json值
     */
    public static JSONObject toJSONObject(String content) {

        if (null == content || "".equals(content)) {
            return null;
        }
        try (InputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"))) {
            return (JSONObject) inputStream2Map(in, null);
        } catch (IOException e) {
            throw new PayErrorException(new PayException("IOException", e.getMessage()));
        }

    }

    public static JSONObject toJSONObject(String content, String charset) {

        if (null == content || "".equals(content)) {
            return null;
        }
        try (InputStream in = new ByteArrayInputStream(content.getBytes(charset))) {
            return (JSONObject) inputStream2Map(in, null);
        } catch (IOException e) {
            throw new PayErrorException(new PayException("IOException", e.getMessage()));
        }

    }

    /**
     * 解析xml并转化为Json值
     *
     * @param content json字符串
     * @param clazz 需要转化的类
     *              @param <T> 返回对应类型
     * @return Json值
     */
    public static <T> T toBean(String content, Class<T> clazz) {

        if (null == content || "".equals(content)) {
            return null;
        }
        try (InputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"))) {
            return inputStream2Bean(in, clazz);
        } catch (IOException e) {
            throw new PayErrorException(new PayException("IOException", e.getMessage()));
        }

    }


    /**
     * 获取子结点的xml
     *
     * @param children 集合
     * @return String 子结点的xml
     */
    public static JSON getChildren(NodeList children) {
        JSON json = null;
        for (int idx = 0; idx < children.getLength(); ++idx) {
            Node node = children.item(idx);
            NodeList nodeList = node.getChildNodes();
            if (node.getNodeType() == Node.ELEMENT_NODE && nodeList.getLength() <= 1) {
                if (null == json) {
                    json = new JSONObject();
                }
                ((JSONObject) json).put(node.getNodeName(), node.getTextContent());
            } else if (node.getNodeType() == Node.ELEMENT_NODE && nodeList.getLength() > 1) {
                if (null == json) {
                    json = new JSONObject();
                }
                if (json instanceof JSONObject) {
                    JSONObject j = ((JSONObject) json);
                    if (j.containsKey(node.getNodeName())) {
                        JSONArray array = new JSONArray();
                        array.add(json);
                        json = array;
                    } else {
                        j.put(node.getNodeName(), getChildren(nodeList));
                    }
                }

                if (json instanceof JSONArray) {
                    JSONObject c = new JSONObject();
                    c.put(node.getNodeName(), getChildren(nodeList));
                    ((JSONArray) json).add(c);
                }
            }
        }

        return json;
    }

    public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);

        return documentBuilderFactory.newDocumentBuilder();
    }

    public static Document newDocument() throws ParserConfigurationException {
        return newDocumentBuilder().newDocument();
    }

    /***
     *  xml 解析成对应的对象
     * @param in 输入流
     * @param clazz 需要转化的类
     * @param <T> 类型
     * @return 对应的对象
     * @throws IOException  xml io转化异常
     */
    public static <T> T inputStream2Bean(InputStream in, Class<T> clazz) throws IOException {
        try {

            DocumentBuilder documentBuilder = newDocumentBuilder();
            Document doc = documentBuilder.parse(in);
            doc.getDocumentElement().normalize();
            NodeList children = doc.getDocumentElement().getChildNodes();
            JSON json = getChildren(children);
            return json.toJavaObject(clazz);
        } catch (Exception e) {
            throw new PayErrorException(new PayException("XML failure", "XML解析失败\n" + e.getMessage()));
        } finally {
            in.close();
        }

    }

    /**
     * @param in xml输入流
     * @param m  参数集
     * @return 整理完成的参数集
     * @throws IOException xml io转化异常
     */
    public static Map inputStream2Map(InputStream in, Map m) throws IOException {
        if (null == m) {
            m = new JSONObject();
        }
        try {
            DocumentBuilder documentBuilder = newDocumentBuilder();
            Document doc = documentBuilder.parse(in);
            doc.getDocumentElement().normalize();

            NodeList children = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < children.getLength(); ++idx) {
                Node node = children.item(idx);
                if(!node.hasChildNodes()) {
                    m.put(node.getNodeName(), node.getTextContent());
                    continue;
                }
                NodeList nodeList = node.getChildNodes();
                if(nodeList.getLength() == 1 && node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                    m.put(node.getNodeName(), node.getTextContent());
                } else if(nodeList.getLength() == 1 && node.getFirstChild().getNodeType() == Node.CDATA_SECTION_NODE) {
                    m.put(node.getNodeName(), node.getTextContent());
                } else {
                    m.put(node.getNodeName(), getChildren(nodeList));
                }
            }
        } catch (Exception e) {
            throw new PayErrorException(new PayException("XML failure", "XML解析失败\n" + e.getMessage()));
        } finally {
            in.close();
        }
        return m;
    }


    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     */
    public static String getMap2Xml(Map<String, Object> data) {
        Document document = null;
        try {
            document = newDocument();
        } catch (ParserConfigurationException e) {
            throw new PayErrorException(new PayException("ParserConfigurationException", e.getLocalizedMessage()));
        }
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                value = "";
            }
            value = value.toString().trim();
            org.w3c.dom.Element filed = document.createElement(entry.getKey());
            filed.appendChild(document.createTextNode(value.toString()));
            root.appendChild(filed);
        }
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 将键值对转换成xml
     * 形如：<?xml version="1.0" encoding="utf-8" standalone="no"?><message merchantId="1" merchantOrderId="1" codeUrl=""/>
     * @param data
     * @param nodeName
     * @return
     */
    public static String map2Xml(Map<String, Object> data, String nodeName) {
        Document document = null;
        try {
            document = newDocument();
        } catch (ParserConfigurationException e) {
            throw new PayErrorException(new PayException("ParserConfigurationException", e.getLocalizedMessage()));
        }
        org.w3c.dom.Element root = document.createElement(nodeName);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();
            if (null == value) {
                value = "";
            }
            value = value.toString().trim();
            root.setAttribute(entry.getKey(), value.toString());
        }
        document.appendChild(root);

        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static JSONObject xml2Map(String xmlStr) {
        ByteArrayInputStream bais = null;
        DataInputStream dis = null;
        JSONObject result  = new JSONObject();
        try {
            bais = new ByteArrayInputStream(xmlStr.getBytes("utf-8"));
            dis = new DataInputStream(bais);

            DocumentBuilder documentBuilder = newDocumentBuilder();
            Document doc = documentBuilder.parse(dis);
            doc.getDocumentElement().normalize();
            NamedNodeMap nnm = doc.getFirstChild().getAttributes();

            for(int j = 0; j < nnm.getLength(); j++) {
                Node nodeitm = nnm.item(j);
                String value = nodeitm.getNodeValue();
                if(StringUtils.isBlank(value)) {
                    value = "";
                }
                result.put(nodeitm.getNodeName(), nodeitm.getNodeValue().trim());
            }
            NodeList clilds = doc.getFirstChild().getChildNodes();
            JSONObject rootMaps = new JSONObject();
            for(int i = 0; i < clilds.getLength(); i ++) {
                Node node = clilds.item(i);
                if(Node.TEXT_NODE == node.getNodeType()) {
                    continue;
                }
                NodeList nodeCList = node.getChildNodes();
                List<JSONObject> clildMaps = new ArrayList<>();
                for(int j = 0; j < nodeCList.getLength(); j++) {
                    Node cNode = nodeCList.item(j);
                    nnm = cNode.getAttributes();
                    JSONObject detialObj = new JSONObject();
                    if(null == nnm) continue;
                    for(int k = 0; k < nnm.getLength(); k ++) {
                        Node nodeitm= nnm.item(k);
                        String value = nodeitm.getNodeValue();
                        detialObj.put(nodeitm.getNodeName(), value);
                    }
                    clildMaps.add(detialObj);
                }
                rootMaps.put(node.getNodeName(), clildMaps);
            }
            result.putAll(rootMaps);
        } catch (Exception e) {
            throw new PayErrorException(new PayException("XML failure", "XML解析失败\n" + e.getMessage()));
        } finally {
            if(dis != null){
                try {
                    dis.close();
                } catch (Exception e2) {}
            }
            if(bais != null){
                try {
                    bais.close();
                } catch (Exception e2) {}
            }
        }

        return result;
    }

}
