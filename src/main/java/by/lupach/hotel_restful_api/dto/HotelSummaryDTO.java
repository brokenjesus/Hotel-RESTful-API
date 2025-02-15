package by.lupach.hotel_restful_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Summary information about a hotel")
public class HotelSummaryDTO {

    @Schema(description = "Unique identifier of the hotel", example = "1")
    private Long id;

    @Schema(description = "Name of the hotel", example = "DoubleTree by Hilton Minsk")
    private String name;

    @Schema(description = "Brief description of the hotel",
            example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...")
    private String description;

    @Schema(description = "Formatted address of the hotel",
            example = "9 Pobediteley Avenue, Minsk, 220004, Belarus")
    private String address;

    @Schema(description = "Contact phone number", example = "+375 17 309-80-00")
    private String phone;
}
