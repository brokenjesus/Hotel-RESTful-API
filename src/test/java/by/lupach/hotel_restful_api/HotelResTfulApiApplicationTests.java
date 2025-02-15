package by.lupach.hotel_restful_api;

import by.lupach.hotel_restful_api.dto.HotelDetailDTO;
import by.lupach.hotel_restful_api.dto.HotelSummaryDTO;
import by.lupach.hotel_restful_api.entities.Hotel;
import by.lupach.hotel_restful_api.repositories.HotelRepository;
import by.lupach.hotel_restful_api.services.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel sampleHotel;

    @BeforeEach
    void setUp() {
        sampleHotel = Hotel.builder()
                .id(1L)
                .name("Test Hotel")
                .brand("Luxury Brand")
                .description("A luxury hotel for testing")
                .amenities(new HashSet<>())
                .build();
    }

    @Test
    void getAllHotelsSummary_ShouldReturnListOfHotels() {
        when(hotelRepository.findAll()).thenReturn(Collections.singletonList(sampleHotel));
        List<HotelSummaryDTO> result = hotelService.getAllHotelsSummary();
        assertEquals(1, result.size());
        assertEquals("Test Hotel", result.get(0).getName());
    }

    @Test
    void getHotelDetail_ShouldReturnHotelDetail() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        HotelDetailDTO result = hotelService.getHotelDetail(1L);
        assertNotNull(result);
        assertEquals("Test Hotel", result.getName());
    }

    @Test
    void getHotelDetail_ShouldThrowException_WhenHotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> hotelService.getHotelDetail(1L));
        assertEquals("Отель с id=1 не найден", exception.getMessage());
    }

    @Test
    void createHotel_ShouldSaveAndReturnHotelSummary() {
        when(hotelRepository.save(any(Hotel.class))).thenReturn(sampleHotel);
        HotelSummaryDTO result = hotelService.createHotel(sampleHotel);
        assertNotNull(result);
        assertEquals("Test Hotel", result.getName());
    }

    @Test
    void addAmenities_ShouldAddAmenitiesToHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(sampleHotel));
        doAnswer(invocation -> {
            Set<String> amenities = sampleHotel.getAmenities();
            amenities.add("Pool");
            return null;
        }).when(hotelRepository).save(any(Hotel.class));

        hotelService.addAmenities(1L, List.of("Pool"));
        assertTrue(sampleHotel.getAmenities().contains("Pool"));
    }
}
