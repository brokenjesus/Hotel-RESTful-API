package by.lupach.hotel_restful_api.controllers;

import by.lupach.hotel_restful_api.dto.HotelDetailDTO;
import by.lupach.hotel_restful_api.dto.HotelSummaryDTO;
import by.lupach.hotel_restful_api.entities.Hotel;
import by.lupach.hotel_restful_api.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property-view")
public class HotelController {

    @Autowired
    private  HotelService hotelService;


    // 1) GET /property-view/hotels
    @GetMapping("/hotels")
    public List<HotelSummaryDTO> getAllHotels() {
        return hotelService.getAllHotelsSummary();
    }

    // 2) GET /property-view/hotels/{id}
    @GetMapping("/hotels/{id}")
    public HotelDetailDTO getHotelById(@PathVariable Long id) {
        return hotelService.getHotelDetail(id);
    }

    // 3) GET /property-view/search?name=...&brand=...&city=...&county=...&amenities=...
    @GetMapping("/search")
    public List<HotelSummaryDTO> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String amenities) {
        return hotelService.searchHotels(name, brand, city, county, amenities);
    }

    // 4) POST /property-view/hotels
    @PostMapping("/hotels")
    public HotelSummaryDTO createHotel(@RequestBody Hotel hotel) {
        return hotelService.createHotel(hotel);
    }

    // 5) POST /property-view/hotels/{id}/amenities
    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<Void> addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok().build();
    }

    // 6) GET /property-view/histogram/{param}
    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}
