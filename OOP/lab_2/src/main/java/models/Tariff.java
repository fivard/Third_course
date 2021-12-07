package models;

public class Tariff {
    private String name;
    private String id;
    private String operatorName;
    private Integer payroll;
    private Integer SMSPrice;
    private CallPrices callPrices;
    private Parameters parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Integer getPayroll() {
        return payroll;
    }

    public void setPayroll(int payroll) {
        this.payroll = payroll;
    }

    public int getSMSPrice() {
        return SMSPrice;
    }

    public void setSMSPrice(int SMSPrice) {
        this.SMSPrice = SMSPrice;
    }

    public CallPrices getCallPrices() {
        return callPrices;
    }

    public void setCallPrices(CallPrices callPrices) {
        this.callPrices = callPrices;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }


}
