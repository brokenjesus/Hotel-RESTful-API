package by.lupach.hotel_restful_api.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTime {
    private String checkIn;
    private String checkOut;
}
