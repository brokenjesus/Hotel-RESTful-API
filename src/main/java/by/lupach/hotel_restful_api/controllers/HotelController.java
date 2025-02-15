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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property-view")
@Tag(name = "Hotel API", description = "Management of hotels and their data")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Operation(summary = "Get a list of all hotels", description = "Returns brief information about all hotels")
    @ApiResponse(responseCode = "200", description = "Hotel list successfully retrieved")
    @GetMapping("/hotels")
    public List<HotelSummaryDTO> getAllHotels() {
        return hotelService.getAllHotelsSummary();
    }

    @Operation(summary = "Get hotel details", description = "Returns detailed information about a hotel by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hotel information retrieved"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    @GetMapping("/hotels/{id}")
    public HotelDetailDTO getHotelById(@Parameter(description = "Hotel ID") @PathVariable Long id) {
        return hotelService.getHotelDetail(id);
    }

    @Operation(summary = "Search hotels", description = "Allows searching for hotels by various parameters")
    @ApiResponse(responseCode = "200", description = "Search results returned")
    @GetMapping("/search")
    public List<HotelSummaryDTO> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String amenities) {
        return hotelService.searchHotels(name, brand, city, county, amenities);
    }

    @Operation(summary = "Create a new hotel", description = "Adds a new hotel to the system")
    @ApiResponse(responseCode = "201", description = "Hotel successfully created")
    @PostMapping("/hotels")
    public ResponseEntity<HotelSummaryDTO> createHotel(@RequestBody Hotel hotel) {
        HotelSummaryDTO savedHotel = hotelService.createHotel(hotel);
        return ResponseEntity.status(201).body(savedHotel);
    }

    @Operation(summary = "Add amenities to a hotel", description = "Adds a list of amenities to the specified hotel")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Amenities successfully added"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<String> addAmenities(
            @Parameter(description = "Hotel ID") @PathVariable Long id,
            @RequestBody List<String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Get histogram", description = "Returns a histogram of data for a specified parameter")
    @ApiResponse(responseCode = "200", description = "Histogram successfully retrieved")
    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}
