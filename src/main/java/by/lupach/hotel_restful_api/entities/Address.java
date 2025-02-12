package by.lupach.hotel_restful_api.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private Integer houseNumber;
    private String street;
    private String city;
    private String county;
    private String postCode;

    @Override
    public String toString() {
        return houseNumber + " " + street + ", " + city + ", " + postCode + ", " + county;
    }
}
