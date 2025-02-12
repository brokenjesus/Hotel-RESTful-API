package by.lupach.hotel_restful_api.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contacts {
    private String phone;
    private String email;
}

