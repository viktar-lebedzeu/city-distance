package org.test.distance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Viktar Lebedzeu
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @Query("from City c where c.name = :cityName")
    Optional<City> findByName(@Param("cityName") String cityName);
}
