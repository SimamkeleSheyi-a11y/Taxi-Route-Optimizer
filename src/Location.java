
import java.util.Objects;

//Represents a location in the taxi route network.
public class Location {

    private final String name;

    public Location(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Location name cannot be empty."
            );
        }
        this.name = name.trim();
    }
    public String getName() {
        return name;
    }
    // Locations with the same name are treated as the same location.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Location)) {
            return false;
        }

        Location other = (Location) obj;
        return name.equalsIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(java.util.Locale.ROOT));
    }
    @Override
    public String toString() {
        return name;
    }
}
