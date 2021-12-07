package parsers;

import models.Tariff;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DOMParser {

    public List<Tariff> parse(File xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(xml);
            document.getDocumentElement().normalize();
            Element rootNode = document.getDocumentElement();
            TariffHandler tariffHandler = new TariffHandler();
            NodeList tariffsNodesList = rootNode.getElementsByTagName(tariffHandler.getName());
            for (int tariffsNode = 0; tariffsNode < tariffsNodesList.getLength(); tariffsNode++) {
                Element tariffElement = (Element) tariffsNodesList.item(tariffsNode);
                traverseNodes(tariffElement, tariffHandler);
            }
            return tariffHandler.getTariffs();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }
    private void traverseNodes(Node node, TariffHandler tariffHandler) {
        if(node.getNodeType() == Node.ELEMENT_NODE) {
            Map<String, String> attributes = new HashMap<>();
            if(node.getAttributes() != null) {
                for (int i = 0; i < node.getAttributes().getLength(); i++) {
                    attributes.put(node.getAttributes().item(i).getNodeName(),
                            node.getAttributes().item(i).getTextContent());
                }
            }
            tariffHandler.setField(node.getNodeName(), node.getTextContent(), attributes);
            if(node.getChildNodes() != null) {
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    traverseNodes(node.getChildNodes().item(i), tariffHandler);
                }
            }
        }
    }

}
