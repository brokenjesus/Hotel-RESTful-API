package by.lupach.hotel_restful_api.controllers;

import by.lupach.hotel_restful_api.dto.HotelDetailDTO;
import by.lupach.hotel_restful_api.dto.HotelSummaryDTO;
import by.lupach.hotel_restful_api.entities.Hotel;
import by.lupach.hotel_restful_api.services.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/property-view")
@Tag(name = "Hotel API", description = "Management of hotels and their data")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(summary = "Create a new hotel", description = "Adds a new hotel to the system")
    @ApiResponse(responseCode = "201", description = "Hotel successfully created")
    @PostMapping("/hotels")
    public ResponseEntity<HotelSummaryDTO> createHotel(@RequestBody Hotel hotel) {
        HotelSummaryDTO savedHotel = hotelService.createHotel(hotel);
        return new ResponseEntity<>(savedHotel, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a list of all hotels", description = "Returns brief information about all hotels")
    @ApiResponse(responseCode = "200", description = "Hotel list successfully retrieved")
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelSummaryDTO>> getAllHotels() {
        return new ResponseEntity<>(hotelService.getAllHotelsSummary(), HttpStatus.OK);
    }

    @Operation(summary = "Get hotel details", description = "Returns detailed information about a hotel by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hotel information retrieved"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelDetailDTO> getHotelById(@Parameter(description = "Hotel ID") @PathVariable Long id) {
        return new ResponseEntity<>(hotelService.getHotelDetail(id), HttpStatus.OK);
    }

    @Operation(summary = "Update hotel details", description = "Updates the details of an existing hotel by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hotel successfully updated"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    @PutMapping("/hotels/{id}")
    public ResponseEntity<HotelSummaryDTO> updateHotel(
            @Parameter(description = "Hotel ID") @PathVariable Long id,
            @RequestBody Hotel hotel) {
        hotel.setId(id);  // Set the hotel ID from the URL path
        HotelSummaryDTO updatedHotel = hotelService.updateHotel(hotel);
        return new ResponseEntity<>(updatedHotel, HttpStatus.OK);
    }

    @Operation(summary = "Delete a hotel", description = "Deletes a hotel from the system by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Hotel successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    @DeleteMapping("/hotels/{id}")
    public ResponseEntity<Void> deleteHotel(@Parameter(description = "Hotel ID") @PathVariable Long id) {
        hotelService.deleteHotel(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Search hotels", description = "Allows searching for hotels by various parameters")
    @ApiResponse(responseCode = "200", description = "Search results returned")
    @GetMapping("/search")
    public ResponseEntity<List<HotelSummaryDTO>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenities) {
        return new ResponseEntity<>(hotelService.searchHotels(name, brand, city, country, amenities), HttpStatus.OK);
    }

    @Operation(summary = "Add amenities to a hotel", description = "Adds a list of amenities to the specified hotel")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Amenities successfully added"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<Void> addAmenities(
            @Parameter(description = "Hotel ID") @PathVariable Long id,
            @RequestBody List<String> amenities) {
        hotelService.addAmenities(id, amenities);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Delete amenities from a hotel", description = "Removes the specified amenities from the hotel")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Amenities successfully removed"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    @DeleteMapping("/hotels/{id}/amenities")
    public ResponseEntity<Void> deleteAmenities(
            @Parameter(description = "Hotel ID") @PathVariable Long id,
            @RequestBody Optional<List<String>> amenities) {
        hotelService.deleteAmenities(id, amenities);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get histogram", description = "Returns a histogram of data for a specified parameter")
    @ApiResponse(responseCode = "200", description = "Histogram successfully retrieved")
    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable String param) {
        return new ResponseEntity<>(hotelService.getHistogram(param), HttpStatus.OK);
    }
}
