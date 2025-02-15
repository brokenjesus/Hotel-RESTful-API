package by.lupach.hotel_restful_api.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Contact details of the hotel")
public class Contacts {

    @Schema(description = "Contact phone number", example = "+375 17 309-80-00")
    private String phone;

    @Schema(description = "Contact email address", example = "doubletreeminsk.info@hilton.com")
    private String email;
}
