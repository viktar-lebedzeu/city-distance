package org.test.distance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Viktar Lebedzeu
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("from Route r where r.cityFrom.name = :fromName and r.cityTo.name = :toName")
    public Optional<Route> findRouteByCityNames(@Param("fromName") String fromName, @Param("toName") String toName);

    @Query("delete from Route r where r.cityFrom.name = :fromName and r.cityTo.name = :toName")
    @Modifying
    public void deleteRoutesByCityNames(@Param("fromName") String fromName, @Param("toName") String toName);

    @Query("delete from Route r where r.cityFrom.name = :fromName")
    @Modifying
    public void deleteRoutesFromCity(@Param("fromName") String fromName);

    @Query("delete from Route r where r.cityTo.name = :toName")
    @Modifying
    public void deleteRoutesToCity(@Param("toName") String toName);

    @Query("delete from Route r where r.cityFrom.id = :fromId and r.cityTo.id = :toId")
    @Modifying
    public void deleteRoutesByCityIds(@Param("fromId") Long fromId, @Param("toId") Long toId);

    @Query("delete from Route r where r.cityFrom.id = :fromId")
    @Modifying
    public void deleteRoutesFromCity(@Param("fromId") Long fromId);

    @Query("delete from Route r where r.cityTo.id = :toId")
    @Modifying
    public void deleteRoutesToCity(@Param("toId") Long toId);

    @Query("from Route r where r.cityFrom.name = :fromName and r.cityTo.name = :toName")
    public List<Route> findRoutesByCityNames(@Param("fromName") String fromName, @Param("toName") String toName);

    @Query("from Route r where r.cityFrom.name = :fromName")
    public List<Route> findRoutesByFromCity(@Param("fromName") String fromName);

    @Query("from Route r where r.cityTo.name = :toName")
    public List<Route> findRoutesByToCity(@Param("toName") String toName);

    @Query("from Route r where r.cityFrom.name in(:fromNames)")
    public List<Route> findRoutesByFromCity(@Param("fromNames") List<String> fromNames);

    @Query("from Route r where r.cityTo.name in(:toNames)")
    public List<Route> findRoutesByToCity(@Param("toNames") List<String> toNames);
}
