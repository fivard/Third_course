package parsers;

import models.Tariff;
import org.xml.sax.SAXException;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

public class XMLSAXParser {
    public List<Tariff> parse(File xml) throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        saxParser = factory.newSAXParser();
        TariffHandler userhandler = new TariffHandler();
        saxParser.parse(xml, userhandler);
        return userhandler.getTariffs();
    }
}