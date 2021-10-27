package task_c;

import java.util.HashMap;
import java.util.Map;

public class Stop {
    private final String city;
    private final Map<Stop, Integer> neighbors;

    public Stop(String city) {
        this.city = city;
        this.neighbors = new HashMap<>();
    }

    public void connect(Stop node, int price) {
        if (this == node) {
            throw new IllegalArgumentException("Can't ride in within one city");
        }
        this.neighbors.put(node, price);
        node.neighbors.put(this, price);
    }

    public void removeConnection(Stop node) {
        neighbors.remove(node);
        node.neighbors.remove(this);
    }

    public boolean isConnected(Stop node) {
        return neighbors.containsKey(node);
    }

    public Map<Stop, Integer> getNeighbors() {
        return neighbors;
    }

    public String getCity() {
        return city;
    }

    public Integer getPrice(Stop other) {
        if (this.neighbors.containsKey(other)) {
            return this.neighbors.get(other);
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this.getClass() != other.getClass()) {
            return false;
        }

        return this.city.equals(((Stop) other).city);
    }
}
