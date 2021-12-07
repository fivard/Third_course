package models;

import java.util.Comparator;

public class TariffComparator implements Comparator<Tariff> {

    @Override
    public int compare(Tariff o1, Tariff o2) {
        return o1.getPayroll().compareTo(o2.getPayroll());
    }
}
