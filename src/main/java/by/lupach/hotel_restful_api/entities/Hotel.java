package by.lupach.hotel_restful_api.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hotels")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Hotel entity representing a hotel record")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the hotel", example = "1")
    private Long id;

    @Schema(description = "Name of the hotel", example = "DoubleTree by Hilton Minsk")
    private String name;

    @Column(length = 2000)
    @Schema(description = "Detailed description of the hotel",
            example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...")
    private String description;

    @Schema(description = "Brand of the hotel", example = "Hilton")
    private String brand;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "houseNumber", column = @Column(name = "house_number")),
            @AttributeOverride(name = "street", column = @Column(name = "street")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "country", column = @Column(name = "country")),
            @AttributeOverride(name = "postCode", column = @Column(name = "post_code"))
    })
    @Schema(description = "Address details of the hotel")
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "phone", column = @Column(name = "phone")),
            @AttributeOverride(name = "email", column = @Column(name = "email"))
    })
    @Schema(description = "Contact details of the hotel")
    private Contacts contacts;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "checkIn", column = @Column(name = "check_in")),
            @AttributeOverride(name = "checkOut", column = @Column(name = "check_out"))
    })
    @Schema(description = "Arrival and departure times for the hotel")
    private ArrivalTime arrivalTime;

    @ElementCollection
    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "amenity")
    @Schema(description = "Set of hotel amenities",
            example = "[\"Free parking\", \"Free WiFi\", \"Non-smoking rooms\", \"Concierge\", \"On-site restaurant\", \"Fitness center\", \"Pet-friendly rooms\", \"Room service\", \"Business center\", \"Meeting rooms\"]")
    private Set<String> amenities = new HashSet<>();
}
