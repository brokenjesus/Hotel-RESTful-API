package by.lupach.hotel_restful_api.services;

import by.lupach.hotel_restful_api.dto.HotelDetailDTO;
import by.lupach.hotel_restful_api.dto.HotelSummaryDTO;
import by.lupach.hotel_restful_api.entities.Hotel;
import by.lupach.hotel_restful_api.repositories.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public List<HotelSummaryDTO> getAllHotelsSummary() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    public HotelDetailDTO getHotelDetail(Long id) {
        return hotelRepository.findById(id)
                .map(this::convertToDetailDTO)
                .orElseThrow(() -> new EntityNotFoundException("Hotel " + id + " not found"));
    }

    public List<HotelSummaryDTO> searchHotels(String name, String brand, String city, String county, String amenity) {
        Specification<Hotel> spec = Specification.where(null);

        if (name != null && !name.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (brand != null && !brand.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("brand")), brand.toLowerCase()));
        }
        if (city != null && !city.isBlank()) {
            spec = spec.and((root, query, cb) -> {
                if (root.get("address") != null) {
                    return cb.equal(cb.lower(root.get("address").get("city")), city.toLowerCase());
                }
                return null;
            });
        }
        if (county != null && !county.isBlank()) {
            spec = spec.and((root, query, cb) -> {
                if (root.get("address") != null) {
                    return cb.equal(cb.lower(root.get("address").get("county")), county.toLowerCase());
                }
                return null;
            });
        }
        if (amenity != null && !amenity.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.isMember(amenity.toLowerCase(), root.get("amenities")));
        }

        List<Hotel> hotels = hotelRepository.findAll(spec);
        return hotels.stream().map(this::convertToSummaryDTO).collect(Collectors.toList());
    }

    public Map<String, Long> getHistogram(String param) {
        Map<String, Long> result = new HashMap<>();
        List<Object[]> queryResult = switch (param.toLowerCase()) {
            case "brand" -> hotelRepository.countByBrand();
            case "city" -> hotelRepository.countByCity();
            case "county" -> hotelRepository.countByCounty();
            case "amenities" -> hotelRepository.countByAmenities();
            default -> throw new IllegalArgumentException("Invalid histogram parameter: " + param);
        };

        for (Object[] row : queryResult) {
            if (row[0] != null) {
                result.put(row[0].toString(), (Long) row[1]);
            }
        }
        return result;
    }

    public HotelSummaryDTO createHotel(Hotel hotel) {
        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToSummaryDTO(savedHotel);
    }

    public void addAmenities(Long id, List<String> amenities) {
        hotelRepository.findById(id)
                .ifPresentOrElse(hotel -> {
                    hotel.getAmenities().addAll(amenities);
                    hotelRepository.save(hotel);
                }, () -> {
                    throw new EntityNotFoundException("Hotel " + id + " not found");
                });
    }

    private HotelSummaryDTO convertToSummaryDTO(Hotel hotel) {
        String address = (hotel.getAddress() != null) ? hotel.getAddress().toString() : "";
        String phone = (hotel.getContacts() != null) ? hotel.getContacts().getPhone() : "";
        return new HotelSummaryDTO(hotel.getId(), hotel.getName(), hotel.getDescription(), address, phone);
    }

    private HotelDetailDTO convertToDetailDTO(Hotel hotel) {
        return new HotelDetailDTO(
                hotel.getId(),
                hotel.getName(),
                hotel.getBrand(),
                hotel.getAddress(),
                hotel.getContacts(),
                hotel.getArrivalTime(),
                hotel.getAmenities()
        );
    }
}
