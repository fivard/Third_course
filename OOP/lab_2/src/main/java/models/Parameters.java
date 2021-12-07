package models;

public class Parameters {
    public String getTariffication() {
        return tariffication;
    }

    public void setTariffication(String tariffication) {
        this.tariffication = tariffication;
    }

    public int getConnectionCost() {
        return connectionCost;
    }

    public void setConnectionCost(int connectionCost) {
        this.connectionCost = connectionCost;
    }

    private String tariffication;
    private int connectionCost;

}
