package task_c;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BusScheduler {
    private final List<Stop> busStops;

    public BusScheduler() {
        busStops = new ArrayList<>();
    }

    public void addBusStop(String city) {
        busStops.add(new Stop(city));
        System.out.println("Added bus stop: " + city);
    }

    public void deleteBusStop(String city) {
        Stop cityStopToDelete = null;
        for (Stop cityStop : busStops) {
            if (cityStop.getCity().equals(city)) {
                cityStopToDelete = cityStop;
                break;
            }
        }
        if (cityStopToDelete == null) {
            return;
        } else {
            Set<Stop> stopsConnectedToDeleted = new HashSet<>(
                    cityStopToDelete.getNeighbors().keySet());
            for (Stop cityStop : stopsConnectedToDeleted) {
                cityStopToDelete.removeConnection(cityStop);
            }
            busStops.remove(cityStopToDelete);
        }
        System.out.println("Deleted bus stop: " + city);
    }

    public void changeFlightPrice(String firstCity, String secondCity, int price) {
        Stop firstCityStopToChange = getCityStopByName(firstCity);
        Stop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange != null && secondCity != null
                && firstCityStopToChange.isConnected(secondCityStopToChange)) {
            firstCityStopToChange.connect(secondCityStopToChange, price);
            System.out.println("Changed price for flight from " + firstCity + " to " + secondCity
                    + " for " + price);
        }
    }

    public void addFlight(String firstCity, String secondCity, int price) {
        Stop firstCityStop = getCityStopByName(firstCity);
        Stop secondCityStop = getCityStopByName(secondCity);
        if (firstCityStop != null && secondCity != null) {
            firstCityStop.connect(secondCityStop, price);
            System.out.println("Added flight from " + firstCity + " to " + secondCity
                    + " with price " + price);
        }
    }

    public void deleteFlight(String firstCity, String secondCity) {
        Stop firstCityStopToChange = getCityStopByName(firstCity);
        Stop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange != null && secondCity != null
                && firstCityStopToChange.isConnected(secondCityStopToChange)) {
            firstCityStopToChange.removeConnection(secondCityStopToChange);
            System.out.println("Deleted flight from " + firstCity + " to " + secondCity);
        }
    }

    public Integer getFlightPrice(String firstCity, String secondCity) {
        Stop firstCityStopToChange = getCityStopByName(firstCity);
        Stop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange == null || secondCityStopToChange == null) {
            throw new IllegalArgumentException("No such bus stop");
        }

        return firstCityStopToChange.getPrice(secondCityStopToChange);
    }

    private Stop getCityStopByName(String cityName) {
        Stop cityStop = new Stop(cityName);
        if (busStops.contains(cityStop)) {
            return busStops.get(busStops.indexOf(cityStop));
        }
        return null;
    }
}
