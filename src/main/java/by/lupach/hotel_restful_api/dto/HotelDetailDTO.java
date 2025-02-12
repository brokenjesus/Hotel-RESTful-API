package by.lupach.hotel_restful_api.dto;
import by.lupach.hotel_restful_api.entities.Address;
import by.lupach.hotel_restful_api.entities.ArrivalTime;
import by.lupach.hotel_restful_api.entities.Contacts;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDetailDTO {
    private Long id;
    private String name;
    private String brand;
    private Address address;
    private Contacts contacts;
    private ArrivalTime arrivalTime;
    private Set<String> amenities;
}