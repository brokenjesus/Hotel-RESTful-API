package by.lupach.hotel_restful_api;

import by.lupach.hotel_restful_api.controllers.HotelController;
import by.lupach.hotel_restful_api.dto.HotelDetailDTO;
import by.lupach.hotel_restful_api.dto.HotelSummaryDTO;
import by.lupach.hotel_restful_api.entities.Address;
import by.lupach.hotel_restful_api.entities.ArrivalTime;
import by.lupach.hotel_restful_api.entities.Contacts;
import by.lupach.hotel_restful_api.entities.Hotel;
import by.lupach.hotel_restful_api.services.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class HotelControllerTest {

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(hotelController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllHotels() throws Exception {
        HotelSummaryDTO hotelSummary1 = new HotelSummaryDTO(
                1L,
                "Hotel 1",
                "Description 1",
                "Address 1",
                "123456789"
        );

        HotelSummaryDTO hotelSummary2 = new HotelSummaryDTO(
                2L,
                "Hotel 2",
                "Description 2",
                "Address 2",
                "987654321"
        );

        List<HotelSummaryDTO> hotelList = List.of(hotelSummary1, hotelSummary2);

        when(hotelService.getAllHotelsSummary()).thenReturn(hotelList);
        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hotel 1"))
                .andExpect(jsonPath("$[1].name").value("Hotel 2"))
                .andExpect(status().isOk());

        verify(hotelService, times(1)).getAllHotelsSummary();
    }

    @Test
    void getHotelById() throws Exception {
        Address hotelAddress = new Address(
                9,
                "Pobediteley Avenue",
                "Minsk",
                "Belarus",
                "220004"
        );

        Contacts hotelContacts = new Contacts("+375 17 309-80-00", "doubletreeminsk.info@hilton.com");
        ArrivalTime hotelArrivalTime = new ArrivalTime("14:00", "12:00");
        Set<String> hotelAmenities = new HashSet<>(Set.of(
                "Free parking",
                "Free WiFi",
                "Non-smoking rooms",
                "Concierge",
                "On-site restaurant",
                "Fitness center",
                "Pet-friendly rooms",
                "Room service",
                "Business center",
                "Meeting rooms"
        ));

        HotelDetailDTO hotelDetail = new HotelDetailDTO(1L,
                "DoubleTree by Hilton Minsk",
                "Hilton",
                hotelAddress,
                hotelContacts,
                hotelArrivalTime,
                hotelAmenities
        );
        when(hotelService.getHotelDetail(1L)).thenReturn(hotelDetail);
        mockMvc.perform(get("/property-view/hotels/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
                .andExpect(status().isOk());

        verify(hotelService, times(1)).getHotelDetail(1L);
    }

    @Test
    void searchHotels() throws Exception {
        HotelSummaryDTO hotelSummary = new HotelSummaryDTO(
                1L,
                "Hotel 1",
                "Description 1",
                "Address 1",
                "123456789"
        );

        List<HotelSummaryDTO> hotelList = List.of(hotelSummary);

        when(hotelService.searchHotels(
                "Hotel 1",
                null,
                null,
                null,
                null)
        ).thenReturn(hotelList);

        mockMvc.perform(get("/property-view/search").param("name", "Hotel 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hotel 1"))
                .andExpect(status().isOk());

        verify(hotelService, times(1))
                .searchHotels("Hotel 1", null, null, null, null);
    }

    @Test
    void createHotel() throws Exception {
        Address hotelAddress = new Address(
                9,
                "Pobediteley Avenue",
                "Minsk",
                "Belarus",
                "220004"
        );

        Contacts hotelContacts = new Contacts("+375 17 309-80-00", "doubletreeminsk.info@hilton.com");
        ArrivalTime hotelArrivalTime = new ArrivalTime("14:00", "12:00");
        Set<String> hotelAmenities = new HashSet<>(Set.of(
                "Free parking",
                "Free WiFi",
                "Non-smoking rooms",
                "Concierge",
                "On-site restaurant",
                "Fitness center",
                "Pet-friendly rooms",
                "Room service",
                "Business center",
                "Meeting rooms"
        ));

        Hotel hotel = new Hotel(1L,
                "DoubleTree by Hilton Minsk",
                "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms " +
                        "in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
                "Hilton",
                hotelAddress,
                hotelContacts,
                hotelArrivalTime,
                hotelAmenities);

        String hotelJson = objectMapper.writeValueAsString(hotel);

        HotelSummaryDTO hotelSummary = new HotelSummaryDTO(
                1L,
                "DoubleTree by Hilton Minsk",
                "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms \" +\n" +
                        "                        \"in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
                "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                "+375 17 309-80-00"
        );

        when(hotelService.createHotel(hotel)).thenReturn(hotelSummary);


        mockMvc.perform(post("/property-view/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(hotelJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"));

        verify(hotelService, times(1)).createHotel(hotel);
    }

    @Test
    void updateHotel() throws Exception {
        Address hotelAddress = new Address(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004");
        Contacts hotelContacts = new Contacts("+375 17 309-80-00", "doubletreeminsk.info@hilton.com");
        ArrivalTime hotelArrivalTime = new ArrivalTime("14:00", "12:00");
        Set<String> hotelAmenities = new HashSet<>(Set.of(
                "Free parking",
                "Free WiFi",
                "Non-smoking rooms"
        ));

        Hotel hotel = new Hotel(1L, "Updated Hotel Name", "Updated Description", "Updated Brand", hotelAddress, hotelContacts, hotelArrivalTime, hotelAmenities);
        String hotelJson = objectMapper.writeValueAsString(hotel);

        HotelSummaryDTO updatedHotelSummary = new HotelSummaryDTO(
                1L,
                "Updated Hotel Name",
                "Updated Description",
                "Updated Brand",
                "+375 17 309-80-00"
        );

        when(hotelService.updateHotel(hotel)).thenReturn(updatedHotelSummary);

        mockMvc.perform(put("/property-view/hotels/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hotelJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Hotel Name"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(hotelService, times(1)).updateHotel(hotel);
    }


    @Test
    void deleteHotel() throws Exception {
        Long hotelId = 1L;

        doNothing().when(hotelService).deleteHotel(hotelId);

        mockMvc.perform(delete("/property-view/hotels/{id}", hotelId))
                .andExpect(status().isNoContent());

        verify(hotelService, times(1)).deleteHotel(hotelId);
    }

    @Test
    void deleteAmenities() throws Exception {
        Long hotelId = 1L;
        List<String> amenities = List.of(
                "Free parking",
                "Free WiFi",
                "Non-smoking rooms"
        );
        String amenitiesJson = objectMapper.writeValueAsString(amenities);

        doNothing().when(hotelService).deleteAmenities(eq(hotelId), any());

        mockMvc.perform(delete("/property-view/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(amenitiesJson))
                .andExpect(status().isNoContent());

        verify(hotelService, times(1)).deleteAmenities(eq(hotelId), eq(Optional.of(amenities)));
    }



    @Test
    void addAmenities() throws Exception {
        List<String> amenities = List.of(
            "Free parking",
            "Free WiFi",
            "Non-smoking rooms",
            "Concierge",
            "On-site restaurant",
            "Fitness center",
            "Pet-friendly rooms",
            "Room service",
            "Business center",
            "Meeting rooms"
        );

        String amenitiesJson = objectMapper.writeValueAsString(amenities);

        mockMvc.perform(post("/property-view/hotels/{id}/amenities", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(amenitiesJson))
                .andExpect(status().isCreated());

        verify(hotelService, times(1)).addAmenities(1L, amenities);
    }

    @Test
    void getHistogram() throws Exception {
        Map<String, Long> histogram = new HashMap<>();
        histogram.put("Minsk", 1L);
        histogram.put("Moskow", 2L);

        when(hotelService.getHistogram("city")).thenReturn(histogram);

        mockMvc.perform(get("/property-view/histogram/{param}", "city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(1L))
                .andExpect(jsonPath("$.Moskow").value(2L));

        verify(hotelService, times(1)).getHistogram("city");
    }


}
