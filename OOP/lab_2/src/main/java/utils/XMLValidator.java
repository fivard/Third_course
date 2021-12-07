package utils;

import org.xml.sax.SAXException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLValidator {
    private static final Logger log = Logger.getLogger(XMLValidator.class.getName());

    public static boolean validateXML(String xmlFile, String xsdFile){
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new File(xsdFile));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlFile)));
        } catch (SAXException | IOException e) {
            log.log(Level.SEVERE, "Exception: ", e);
            return false;
        }
        return true;
    }
}