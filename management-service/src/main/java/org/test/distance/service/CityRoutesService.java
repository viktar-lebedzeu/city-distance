package org.test.distance.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.test.distance.dao.City;
import org.test.distance.dao.CityRepository;
import org.test.distance.dao.Route;
import org.test.distance.dao.RouteRepository;
import org.test.distance.dto.RouteDto;
import org.test.distance.dto.RouteIdsRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service that provides methods to work with repository entities (cities and routes
 * @author Viktar Lebedzeu
 */
@Service
@Transactional
public class CityRoutesService {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(CityRoutesService.class);

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Transactional(readOnly = true)
    public Long countAllCities() {
        return cityRepository.count();
    }

    @Transactional(timeout = 2 * 60 * 60, propagation = Propagation.REQUIRED)
    public City createOrMergeCity(City city) {
        return cityRepository.saveAndFlush(city);
    }

    @Transactional(readOnly = true)
    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    @Transactional(timeout = 2 * 60 * 60, propagation = Propagation.REQUIRED)
    public void deleteCity(Long id) {
        cityRepository.findById(id).ifPresent(city -> cityRepository.delete(city));
    }

    @Transactional(timeout = 2 * 60 * 60, propagation = Propagation.REQUIRED)
    public void deleteCityByName(String cityName) {
        cityRepository.findByName(cityName).ifPresent(city -> cityRepository.delete(city));
    }

    @Transactional(timeout = 2 * 60 * 60, propagation = Propagation.REQUIRED)
    public boolean createOrUpdateRoute(RouteDto route) {
        try {
            Optional<City> cityFrom = cityRepository.findByName(route.getFrom());
            Optional<City> cityTo = cityRepository.findByName(route.getTo());
            Optional<Route> directRoute = routeRepository.findRouteByCityNames(route.getFrom(), route.getTo());
            if (!cityFrom.isPresent()) {
                cityFrom = Optional.of(cityRepository.saveAndFlush(new City(null, route.getFrom(), null)));
            }
            if (!cityTo.isPresent()) {
                cityTo = Optional.of(cityRepository.saveAndFlush(new City(null, route.getTo(), null)));
            }

            Route updatedDirectRoute = directRoute.orElse(
                    new Route(null, cityFrom.get(), cityTo.get(), route.getDistance()));
            directRoute = Optional.of(routeRepository.saveAndFlush(updatedDirectRoute));

            if (route.isTwoWay()) {
                Optional<Route> reverseRoute = routeRepository.findRouteByCityNames(route.getTo(), route.getFrom());
                Route updatedReverseRoute = reverseRoute.orElse(
                        new Route(null, cityTo.get(), cityFrom.get(), route.getDistance()));
                reverseRoute = Optional.of(routeRepository.saveAndFlush(updatedReverseRoute));
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<RouteDto> findRoutes() {
        final List<Route> routes = routeRepository.findAll();
        return routes.parallelStream()
            .map(route ->  new RouteDto(route.getCityFrom().getName(), route.getCityTo().getName(), route.getDistance())
        ).collect(Collectors.toList());
    }

    @Transactional(timeout = 2 * 60 * 60, propagation = Propagation.REQUIRED)
    public void deleteRoutes(RouteDto routeDto) {
        if (StringUtils.isEmpty(routeDto.getFrom()) && StringUtils.isEmpty(routeDto.getTo())) {
            routeRepository.deleteAll();
            return;
        }
        Optional<City> fromCity = StringUtils.isNotEmpty(routeDto.getFrom())
                ? cityRepository.findByName(routeDto.getFrom())
                : Optional.empty();
        Optional<City> toCity = StringUtils.isNotEmpty(routeDto.getTo())
                ? cityRepository.findByName(routeDto.getTo())
                : Optional.empty();

        if (StringUtils.isNotEmpty(routeDto.getFrom()) && StringUtils.isNotEmpty(routeDto.getTo())
                && fromCity.isPresent() && toCity.isPresent()) {
            routeRepository.deleteRoutesByCityIds(fromCity.get().getId(), toCity.get().getId());
            if (routeDto.isTwoWay()) {
                routeRepository.deleteRoutesByCityIds(toCity.get().getId(), fromCity.get().getId());
            }
            return;
        }
        if (StringUtils.isNotEmpty(routeDto.getFrom()) && fromCity.isPresent()) {
            routeRepository.deleteRoutesFromCity(fromCity.get().getId());
            if (routeDto.isTwoWay()) {
                routeRepository.deleteRoutesToCity(fromCity.get().getId());
            }
            return;
        }
        if (StringUtils.isNotEmpty(routeDto.getTo()) && toCity.isPresent()) {
            routeRepository.deleteRoutesToCity(toCity.get().getId());
            if (routeDto.isTwoWay()) {
                routeRepository.deleteRoutesFromCity(toCity.get().getId());
            }
        }
    }

    @Transactional(readOnly = true)
    public List<RouteDto> findRoutes(RouteDto routeDto) {
        List<Route> routes = null;
        if (StringUtils.isEmpty(routeDto.getFrom()) && StringUtils.isEmpty(routeDto.getTo())) {
            routes = routeRepository.findAll();
        }
        else if (StringUtils.isNotEmpty(routeDto.getFrom()) && StringUtils.isNotEmpty(routeDto.getTo())) {
            routes = routeRepository.findRoutesByCityNames(routeDto.getFrom(), routeDto.getTo());
        }
        else if (StringUtils.isNotEmpty(routeDto.getFrom())) {
            routes = routeRepository.findRoutesByFromCity(routeDto.getFrom());
        }
        else if (StringUtils.isNotEmpty(routeDto.getTo())) {
            routes = routeRepository.findRoutesByToCity(routeDto.getTo());
        }
        if (routes != null && !routes.isEmpty()) {
            return routes.parallelStream()
                    .map(r -> new RouteDto(r.getCityFrom().getName(), r.getCityTo().getName(), r.getDistance()))
                    .collect(Collectors.toList());
        }
        else {
            return new ArrayList<>(0);
        }
    }

    @Transactional(readOnly = true)
    public List<RouteDto> findRoutes(RouteIdsRequestDto routeDto) {
        if (routeDto == null || routeDto.getIds() == null || routeDto.getIds().isEmpty()) {
            return new ArrayList<>(0);
        }
        List<Route> routes = (routeDto.isReverse())
                ? routeRepository.findRoutesByToCity(routeDto.getIds())
                : routeRepository.findRoutesByFromCity(routeDto.getIds());
        return routes.stream()
                .map(r -> new RouteDto(r.getCityFrom().getName(), r.getCityTo().getName(), r.getDistance()))
                .collect(Collectors.toList());
    }
}
