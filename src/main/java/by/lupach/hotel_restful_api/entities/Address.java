package by.lupach.hotel_restful_api.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Address details of the hotel")
public class Address {

    @Schema(description = "House number", example = "9")
    private Integer houseNumber;

    @Schema(description = "Street name", example = "Pobediteley Avenue")
    private String street;

    @Schema(description = "City", example = "Minsk")
    private String city;

    @Schema(description = "County or region", example = "Belarus")
    private String county;

    @Schema(description = "Postal code", example = "220004")
    private String postCode;

    @Override
    public String toString() {
        return houseNumber + " " + street + ", " + city + ", " + postCode + ", " + county;
    }
}
