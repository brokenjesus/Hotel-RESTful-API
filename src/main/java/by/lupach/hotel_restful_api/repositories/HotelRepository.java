package by.lupach.hotel_restful_api.repositories;

import by.lupach.hotel_restful_api.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    @Query("SELECT h.brand, COUNT(h) FROM Hotel h GROUP BY h.brand")
    List<Object[]> countByBrand();

    @Query("SELECT h.address.city, COUNT(h) FROM Hotel h GROUP BY h.address.city")
    List<Object[]> countByCity();

    @Query("SELECT h.address.county, COUNT(h) FROM Hotel h GROUP BY h.address.county")
    List<Object[]> countByCounty();

    @Query("SELECT a, COUNT(h) FROM Hotel h JOIN h.amenities a GROUP BY a")
    List<Object[]> countByAmenities();
}
