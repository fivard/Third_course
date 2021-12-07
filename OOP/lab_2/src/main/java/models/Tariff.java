package models;

public class Tariff implements Comparable<Tariff>{
    private String name;
    private String operatorName;
    private int id;
    private int payroll;
    private int SMSPrice;
    private int inNetworkPrice;
    private int outNetworkPrice;
    private int stationaryPrice;
    private int tariffication;
    private  int connectionCost;

    @Override
    public int compareTo(Tariff o) {
        return 0;
    }
}
