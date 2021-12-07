package parsers;

import models.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TariffHandler extends DefaultHandler{

    private String elementValue;
    private List<Tariff> listOfTariff = new ArrayList<>();
    public List<Tariff> getTariffs() {
        return listOfTariff;
    }

    @Override
    public void startDocument() throws SAXException {
        listOfTariff = new ArrayList<>();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        elementValue = new String(ch, start, length);
    }

    public String getName() {
        return XMLElements.TARIFF;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case XMLElements.TARIFF:
                Tariff tariff = new Tariff();
                listOfTariff.add(tariff);
                break;
            case XMLElements.CALLPRICES:
                CallPrices callPrices = new CallPrices();
                getLastTariff().setCallPrices(callPrices);
                break;
            case XMLElements.PARAMETERS:
                Parameters parameters = new Parameters();
                getLastTariff().setParameters(parameters);
                break;
        }
    }

    private Tariff getLastTariff() {
        return listOfTariff.get(listOfTariff.size() - 1);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case XMLElements.ID:
                getLastTariff().setId(elementValue);
                break;
            case XMLElements.NAME:
                getLastTariff().setName(elementValue);
                break;
            case XMLElements.OPERATORNAME:
                getLastTariff().setOperatorName(elementValue);
                break;
            case XMLElements.PAYROLL:
                getLastTariff().setPayroll(Integer.parseInt(elementValue));
                break;
            case XMLElements.SMSPRICE:
                getLastTariff().setSMSPrice(Integer.parseInt(elementValue));
                break;
            case XMLElements.INNETWORK:
                getLastTariff().getCallPrices().setInNetwork(Integer.valueOf(elementValue));
                break;
            case XMLElements.OUTNETWORK:
                getLastTariff().getCallPrices().setOutNetwork(Integer.valueOf(elementValue));
                break;
            case XMLElements.STATIONARY:
                getLastTariff().getCallPrices().setStationary(Integer.valueOf(elementValue));
                break;
            case XMLElements.TARIFFICATION:
                getLastTariff().getParameters().setTariffication(elementValue);
                break;
            case XMLElements.CONNECTIONCOST:
                getLastTariff().getParameters().setConnectionCost(Integer.parseInt(elementValue));
                break;

        }
    }

    public void setField(String qName, String content, Map<String ,String> attributes) {
        switch (qName) {
            case XMLElements.TARIFF:
                Tariff tariff = new Tariff();
                listOfTariff.add(tariff);
                break;
            case XMLElements.CALLPRICES:
                CallPrices callPrices = new CallPrices();
                getLastTariff().setCallPrices(callPrices);
                break;
            case XMLElements.PARAMETERS:
                Parameters parameters = new Parameters();
                getLastTariff().setParameters(parameters);
                break;
            case XMLElements.NAME:
                getLastTariff().setName(content);
                break;
            case XMLElements.ID:
                getLastTariff().setId(content);
                break;
            case XMLElements.OPERATORNAME:
                getLastTariff().setOperatorName(content);
                break;
            case XMLElements.PAYROLL:
                getLastTariff().setPayroll(Integer.parseInt(content));
                break;
            case XMLElements.SMSPRICE:
                getLastTariff().setSMSPrice(Integer.parseInt(content));
                break;
            case XMLElements.INNETWORK:
                getLastTariff().getCallPrices().setInNetwork(Integer.valueOf(content));
                break;
            case XMLElements.OUTNETWORK:
                getLastTariff().getCallPrices().setOutNetwork(Integer.valueOf(content));
                break;
            case XMLElements.STATIONARY:
                getLastTariff().getCallPrices().setStationary(Integer.valueOf(content));
                break;
            case XMLElements.TARIFFICATION:
                getLastTariff().getParameters().setTariffication(content);
                break;
            case XMLElements.CONNECTIONCOST:
                getLastTariff().getParameters().setConnectionCost(Integer.parseInt(content));
                break;
        }
    }
}
