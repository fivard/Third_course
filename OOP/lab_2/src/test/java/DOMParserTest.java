import models.*;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import parsers.DOMParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DOMParserTest {

    @Test
    void parse() throws NullPointerException {
        DOMParser parser = new DOMParser();
        File file = new File("src/main/resources/tariffs.xml");
        List<Tariff> tariffs = parser.parse(file);
        Tariff tariff = tariffs.get(0);
        assertEquals(tariff.getCallPrices().getInNetwork(), 5);
        assertEquals(tariff.getCallPrices().getOutNetwork(), 10);
        assertEquals(tariff.getCallPrices().getStationary(), 100);
        assertEquals(XMLElements.IDOfFirstTariff, tariff.getId());
        assertEquals(XMLElements.NameOfFirstTariff, tariff.getName());
        assertEquals(100, tariff.getPayroll());
        assertEquals(tariff.getParameters().getTariffication(), "Minute");
        assertEquals(tariff.getParameters().getConnectionCost(), 2);

        Tariff tariff2 = tariffs.get(1);

        assertEquals(tariff2.getCallPrices().getInNetwork(), 3);
        assertEquals(tariff2.getCallPrices().getOutNetwork(), 5);
        assertEquals(tariff2.getCallPrices().getStationary(), 10);
        assertEquals(XMLElements.IDOfSecondTariff, tariff2.getId());
        assertEquals(XMLElements.NameOfSecondTariff, tariff2.getName());
        assertEquals(150, tariff2.getPayroll());
        assertEquals(tariff2.getParameters().getTariffication(),"Sec");
        assertEquals(tariff2.getParameters().getConnectionCost(),2);
    }
}
