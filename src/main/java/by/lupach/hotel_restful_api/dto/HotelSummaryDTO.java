package by.lupach.hotel_restful_api.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelSummaryDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}