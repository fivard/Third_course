package models;

import java.util.Collections;
import java.util.List;

public class Utils {
    public static void sortTariffs(List<Tariff> tariffs) {
        Collections.sort(tariffs, new TariffComparator());
    }
}
