package by.lupach.hotel_restful_api;

import by.lupach.hotel_restful_api.controllers.HotelController;
import by.lupach.hotel_restful_api.dto.HotelDetailDTO;
import by.lupach.hotel_restful_api.dto.HotelSummaryDTO;
import by.lupach.hotel_restful_api.entities.Address;
import by.lupach.hotel_restful_api.entities.ArrivalTime;
import by.lupach.hotel_restful_api.entities.Contacts;
import by.lupach.hotel_restful_api.entities.Hotel;
import by.lupach.hotel_restful_api.repositories.HotelRepository;
import by.lupach.hotel_restful_api.services.HotelService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;

    @BeforeEach
    public void setup() {
        hotel = Hotel.builder()
                .id(1L)
                .name("DoubleTree by Hilton Minsk")
                .description("The DoubleTree by Hilton Hotel Minsk offers luxurious rooms...")
                .brand("Hilton")
                .address(new Address(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004"))
                .contacts(new Contacts("+375 17 309-80-00", "doubletreeminsk.info@hilton.com"))
                .arrivalTime(new ArrivalTime("14:00", "12:00"))
                .amenities(new HashSet<>(Arrays.asList("Free parking", "Free WiFi")))
                .build();
    }

    @Test
    void testGetAllHotelsSummary() {
        List<Hotel> hotels = Arrays.asList(hotel);
        when(hotelRepository.findAll()).thenReturn(hotels);

        List<HotelSummaryDTO> summaries = hotelService.getAllHotelsSummary();

        assertEquals(1, summaries.size());
        HotelSummaryDTO summary = summaries.get(0);
        assertEquals(hotel.getId(), summary.getId());
        assertEquals(hotel.getName(), summary.getName());
        assertEquals(hotel.getAddress().toString(), summary.getAddress());
        assertEquals(hotel.getContacts().getPhone(), summary.getPhone());

        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testGetHotelDetailFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        HotelDetailDTO detailDTO = hotelService.getHotelDetail(1L);

        assertNotNull(detailDTO);
        assertEquals(hotel.getId(), detailDTO.getId());
        assertEquals(hotel.getName(), detailDTO.getName());
        assertEquals(hotel.getBrand(), detailDTO.getBrand());
        assertEquals(hotel.getAddress(), detailDTO.getAddress());
        assertEquals(hotel.getContacts(), detailDTO.getContacts());
        assertEquals(hotel.getArrivalTime(), detailDTO.getArrivalTime());
        assertEquals(hotel.getAmenities(), detailDTO.getAmenities());

        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void testGetHotelDetailNotFound() {
        when(hotelRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> hotelService.getHotelDetail(2L));
        assertTrue(exception.getMessage().contains("Hotel 2 not found"));

        verify(hotelRepository, times(1)).findById(2L);
    }

    @Test
    void testSearchHotels() {
        List<Hotel> hotels = Arrays.asList(hotel);
        when(hotelRepository.findAll(any(Specification.class))).thenReturn(hotels);

        List<HotelSummaryDTO> summaries = hotelService.searchHotels("DoubleTree", "Hilton", "Minsk", "Belarus", "Free WiFi");

        assertEquals(1, summaries.size());
        HotelSummaryDTO summary = summaries.get(0);
        assertEquals(hotel.getId(), summary.getId());
        assertEquals(hotel.getName(), summary.getName());

        verify(hotelRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testGetHistogramBrand() {
        List<Object[]> queryResult = new ArrayList<>();
        queryResult.add(new Object[]{"Hilton", 5L});
        queryResult.add(new Object[]{"Marriott", 3L});

        when(hotelRepository.countByBrand()).thenReturn(queryResult);

        Map<String, Long> histogram = hotelService.getHistogram("brand");

        assertEquals(2, histogram.size());
        assertEquals(5L, histogram.get("Hilton"));
        assertEquals(3L, histogram.get("Marriott"));

        verify(hotelRepository, times(1)).countByBrand();
    }

    @Test
    void testGetHistogramInvalidParam() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hotelService.getHistogram("invalid"));
        assertTrue(exception.getMessage().contains("Invalid histogram parameter"));
    }

    @Test
    void testCreateHotel() {
        Hotel hotelToSave = Hotel.builder()
                .name("New Hotel")
                .description("New Description")
                .brand("BrandX")
                .build();

        Hotel savedHotel = Hotel.builder()
                .id(10L)
                .name("New Hotel")
                .description("New Description")
                .brand("BrandX")
                .build();

        when(hotelRepository.save(hotelToSave)).thenReturn(savedHotel);

        HotelSummaryDTO summaryDTO = hotelService.createHotel(hotelToSave);

        assertNotNull(summaryDTO);
        assertEquals(savedHotel.getId(), summaryDTO.getId());
        assertEquals(savedHotel.getName(), summaryDTO.getName());

        verify(hotelRepository, times(1)).save(hotelToSave);
    }

    @Test
    void testAddAmenitiesSuccess() {
        Hotel hotelWithAmenities = Hotel.builder()
                .id(1L)
                .amenities(new HashSet<>(Arrays.asList("Free parking")))
                .build();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotelWithAmenities));
        when(hotelRepository.save(any(Hotel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<String> newAmenities = Arrays.asList("Free WiFi", "Pool");
        hotelService.addAmenities(1L, newAmenities);

        ArgumentCaptor<Hotel> hotelCaptor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelRepository).save(hotelCaptor.capture());
        Hotel savedHotel = hotelCaptor.getValue();

        assertTrue(savedHotel.getAmenities().containsAll(newAmenities));
        assertTrue(savedHotel.getAmenities().contains("Free parking"));

        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void testAddAmenitiesNotFound() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());
        List<String> amenities = Arrays.asList("Free WiFi");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> hotelService.addAmenities(99L, amenities));

        assertTrue(exception.getMessage().contains("Hotel 99 not found"));
        verify(hotelRepository, times(1)).findById(99L);
    }
}
