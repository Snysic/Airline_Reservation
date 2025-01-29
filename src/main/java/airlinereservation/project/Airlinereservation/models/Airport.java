package airlinereservation.project.Airlinereservation.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    public Airport() {
    }

    public Airport(String name, String code, String city, String country) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(name, airport.name) &&
               Objects.equals(code, airport.code) &&
               Objects.equals(city, airport.city) &&
               Objects.equals(country, airport.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, city, country);
    }
}
