import controllers.DOMController;
import controllers.SAXController;
import controllers.STAXController;
import utils.XMLCreator;
import utils.XMLValidator;

import java.util.logging.Logger;

public class Demo {
    private static final Logger log = Logger.getLogger(Demo.class.getName());

    public static void main(String[] args) {
        XMLCreator xmlCreator = new XMLCreator();
        DOMController domParser = new DOMController(xmlCreator);
        SAXController saxDrugParser = new SAXController(xmlCreator);
        STAXController staxParser = new STAXController(xmlCreator);

        String XML = "src/main/resources/input.xml";
        String XSD = "src/main/resources/input.xsd";
        if(XMLValidator.validateXML(XML, XSD)){
            log.info("XML is valid");
        }
        else {
            log.info("XML is not valid");
        }
        saxDrugParser.parse(XML);
        saxDrugParser.createXML();
        staxParser.parse(XML);
        staxParser.createXML();
        domParser.parse(XML);
        domParser.createXML();
    }
}