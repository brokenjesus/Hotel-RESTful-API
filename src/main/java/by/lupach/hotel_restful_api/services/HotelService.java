package by.lupach.hotel_restful_api.services;

import by.lupach.hotel_restful_api.dto.HotelDetailDTO;
import by.lupach.hotel_restful_api.dto.HotelSummaryDTO;
import by.lupach.hotel_restful_api.entities.Hotel;
import by.lupach.hotel_restful_api.repositories.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public HotelSummaryDTO createHotel(Hotel hotel) {
        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToSummaryDTO(savedHotel);
    }

    public List<HotelSummaryDTO> getAllHotelsSummary() {
        return hotelRepository.findAll().stream()
                .map(this::convertToSummaryDTO)
                .toList();
    }

    public HotelDetailDTO getHotelDetail(Long id) {
        return hotelRepository.findById(id)
                .map(this::convertToDetailDTO)
                .orElseThrow(() -> new EntityNotFoundException("Hotel " + id + " not found"));
    }

    public HotelSummaryDTO updateHotel(Hotel hotel) {
        hotelRepository.findById(hotel.getId()).orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        Hotel updatedHotel = hotelRepository.save(hotel);
        return convertToSummaryDTO(updatedHotel);
    }

    public void deleteHotel(Long id) {
        hotelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        hotelRepository.deleteById(id);
    }


    public List<HotelSummaryDTO> searchHotels(String name, String brand, String city, String country, String amenity) {
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
        if (country != null && !country.isBlank()) {
            spec = spec.and((root, query, cb) -> {
                if (root.get("address") != null) {
                    return cb.equal(cb.lower(root.get("address").get("country")), country.toLowerCase());
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
            case "country" -> hotelRepository.countByCountry();
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

    public void addAmenities(Long id, List<String> amenities) {
        hotelRepository.findById(id)
                .ifPresentOrElse(hotel -> {
                    hotel.getAmenities().addAll(amenities);
                    hotelRepository.save(hotel);
                }, () -> {
                    throw new EntityNotFoundException("Hotel " + id + " not found");
                });
    }

    public void deleteAmenities(Long id, Optional<List<String>> amenities) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel " + id + " not found"));
        if (amenities.isEmpty()){
            hotel.setAmenities(new HashSet<>());
        }else{
            amenities.get().forEach(hotel.getAmenities()::remove);
        }
        hotelRepository.save(hotel);
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
