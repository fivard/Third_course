package utils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.BeerHandler;
import models.Beer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLCreator {
    private static final Logger log = Logger.getLogger(XMLCreator.class.getName());

    public void buildXML(List<Beer> beerList, String xmlFilePath) {
        try {
            File file = new File(xmlFilePath);
            if (file.createNewFile()) {
                log.info("File created: " + file.getName());
            } else {
                log.info(String.format("File %s already exists.", xmlFilePath));
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Exception: ", e);
        }

        Collections.sort(beerList);

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement(BeerHandler.BEER_SHOP);
            document.appendChild(root);
            for (Beer beer : beerList) {
                Element beerElement = document.createElement(BeerHandler.BEER);
                root.appendChild(beerElement);
                Element id = document.createElement(BeerHandler.ID);
                id.appendChild(document.createTextNode(String.valueOf(beer.getId())));
                beerElement.appendChild(id);
                Element name = document.createElement(BeerHandler.NAME);
                name.appendChild(document.createTextNode(beer.getName()));
                beerElement.appendChild(name);
                Element type = document.createElement(BeerHandler.TYPE);
                type.appendChild(document.createTextNode(beer.getType()));
                beerElement.appendChild(type);
                Element isAlcohol = document.createElement(BeerHandler.ALCOHOL);
                isAlcohol.appendChild(document.createTextNode(String.valueOf((beer.isAlcohol()))));
                beerElement.appendChild(isAlcohol);
                Element manufacturer = document.createElement(BeerHandler.MANUFACTURER);
                manufacturer.appendChild(document.createTextNode(beer.getManufacturer()));
                beerElement.appendChild(manufacturer);
                for (String i : beer.getIngredient()){
                    Element ingredient = document.createElement(BeerHandler.INGREDIENT);
                    ingredient.appendChild(document.createTextNode(i));
                    beerElement.appendChild(ingredient);
                }
                Element chars = document.createElement(BeerHandler.CHARS);
                beerElement.appendChild(chars);
                Element brilliance = document.createElement(BeerHandler.BRILLIANCE);
                brilliance.appendChild(document.createTextNode(String.valueOf(beer.getChars().getBrilliance())));
                chars.appendChild(brilliance);
                Element revolutions = document.createElement(BeerHandler.REVOLUTIONS);
                revolutions.appendChild(document.createTextNode(String.valueOf(beer.getChars().getRevolutions())));
                chars.appendChild(revolutions);
                Element filtered = document.createElement(BeerHandler.FILTERED);
                filtered.appendChild(document.createTextNode(String.valueOf(beer.getChars().isFiltered())));
                chars.appendChild(filtered);
                Element nutritionValue = document.createElement(BeerHandler.NUTRITION_VALUE);
                nutritionValue.appendChild(document.createTextNode(String.valueOf(beer.getChars().getNutritionValue())));
                chars.appendChild(nutritionValue);
                Element containerVolume = document.createElement(BeerHandler.CONTAINER_VOLUME);
                containerVolume.appendChild(document.createTextNode(String.valueOf(beer.getChars().getContainerVolume())));
                chars.appendChild(containerVolume);
                Element containerMaterial = document.createElement(BeerHandler.CONTAINER_MATERIAL);
                containerMaterial.appendChild(document.createTextNode(beer.getChars().getContainerMaterial()));
                chars.appendChild(containerMaterial);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            Result streamResult = new StreamResult(new File(xmlFilePath));
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException e) {
            log.log(Level.SEVERE, "Exception: ", e);
        }
    }
}