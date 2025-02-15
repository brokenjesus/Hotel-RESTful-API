package by.lupach.hotel_restful_api.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Arrival and departure times for the hotel")
public class ArrivalTime {

    @Schema(description = "Check-in time", example = "14:00")
    private String checkIn;

    @Schema(description = "Check-out time", example = "12:00")
    private String checkOut;
}
