package by.lupach.hotel_restful_api.services;

import by.lupach.hotel_restful_api.dto.HotelDetailDTO;
import by.lupach.hotel_restful_api.dto.HotelSummaryDTO;
import by.lupach.hotel_restful_api.entities.Hotel;
import by.lupach.hotel_restful_api.repositories.HotelRepository;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;


    // Получение списка всех отелей (краткая информация)
    public List<HotelSummaryDTO> getAllHotelsSummary() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    // Получение расширенной информации об отеле по id
    public HotelDetailDTO getHotelDetail(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отель с id=" + id + " не найден"));
        return convertToDetailDTO(hotel);
    }

    // Поиск отелей по параметрам
    public List<HotelSummaryDTO> searchHotels(String name, String brand, String city, String county, String amenity) {
        Specification<Hotel> spec = Specification.where(null);

        if (name != null && !name.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (brand != null && !brand.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("brand")), brand.toLowerCase()));
        }
        if (city != null && !city.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("address").get("city")), city.toLowerCase()));
        }
        if (county != null && !county.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("address").get("county")), county.toLowerCase()));
        }
        if (amenity != null && !amenity.isBlank()) {
            spec = spec.and((root, query, cb) -> {
                Join<Hotel, String> joinAmenities = root.join("amenities");
                return cb.equal(cb.lower(joinAmenities), amenity.toLowerCase());
            });
        }

        List<Hotel> hotels = hotelRepository.findAll(spec);
        return hotels.stream().map(this::convertToSummaryDTO).collect(Collectors.toList());
    }

    // Создание нового отеля
    public HotelSummaryDTO createHotel(Hotel hotel) {
        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToSummaryDTO(savedHotel);
    }

    // Добавление списка amenities к отелю
    public void addAmenities(Long id, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отель с id=" + id + " не найден"));
        hotel.getAmenities().addAll(amenities);
        hotelRepository.save(hotel);
    }

    // Получение гистограммы по указанному параметру
    public Map<String, Long> getHistogram(String param) {
        Map<String, Long> result = new HashMap<>();
        List<Object[]> queryResult;
        switch (param.toLowerCase()) {
            case "brand":
                queryResult = hotelRepository.countByBrand();
                for (Object[] row : queryResult) {
                    result.put((String) row[0], (Long) row[1]);
                }
                break;
            case "city":
                queryResult = hotelRepository.countByCity();
                for (Object[] row : queryResult) {
                    result.put((String) row[0], (Long) row[1]);
                }
                break;
            case "county":
                queryResult = hotelRepository.countByCounty();
                for (Object[] row : queryResult) {
                    result.put((String) row[0], (Long) row[1]);
                }
                break;
            case "amenities":
                queryResult = hotelRepository.countByAmenities();
                for (Object[] row : queryResult) {
                    result.put((String) row[0], (Long) row[1]);
                }
                break;
            default:
                throw new IllegalArgumentException("Неверный параметр гистограммы: " + param);
        }
        return result;
    }

    // Преобразование Hotel в краткий DTO
    private HotelSummaryDTO convertToSummaryDTO(Hotel hotel) {
        String address = (hotel.getAddress() != null) ? hotel.getAddress().toString() : "";
        String phone = (hotel.getContacts() != null) ? hotel.getContacts().getPhone() : "";
        return new HotelSummaryDTO(hotel.getId(), hotel.getName(), hotel.getDescription(), address, phone);
    }

    // Преобразование Hotel в детальный DTO
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
