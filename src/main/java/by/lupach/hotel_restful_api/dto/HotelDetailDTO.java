package by.lupach.hotel_restful_api.dto;

import by.lupach.hotel_restful_api.entities.Address;
import by.lupach.hotel_restful_api.entities.ArrivalTime;
import by.lupach.hotel_restful_api.entities.Contacts;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detailed information about a hotel")
public class HotelDetailDTO {

    @Schema(description = "Unique identifier of the hotel", example = "1")
    private Long id;

    @Schema(description = "Name of the hotel", example = "DoubleTree by Hilton Minsk")
    private String name;

    @Schema(description = "Brand of the hotel", example = "Hilton")
    private String brand;

    @Schema(description = "Address details of the hotel")
    private Address address;

    @Schema(description = "Contact details of the hotel")
    private Contacts contacts;

    @Schema(description = "Arrival and departure times")
    private ArrivalTime arrivalTime;

    @Schema(description = "Set of hotel amenities",
            example = "[\"Free parking\", \"Free WiFi\", \"Non-smoking rooms\", \"Concierge\", \"On-site restaurant\", \"Fitness center\", \"Pet-friendly rooms\", \"Room service\", \"Business center\", \"Meeting rooms\"]")
    private Set<String> amenities;
}
