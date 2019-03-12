package org.test.distance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.distance.dao.City;
import org.test.distance.dto.BaseListRequest;
import org.test.distance.dto.BaseResponse;
import org.test.distance.dto.ResponseFactory;
import org.test.distance.dto.RouteDto;
import org.test.distance.dto.RouteIdsRequestDto;
import org.test.distance.service.CityRoutesService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Management REST controller
 * @author Viktar Lebedzeu
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ManagementController {
    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);

    @Autowired
    private CityRoutesService cityRoutesService;

    @PostMapping("/city/add")
    public ResponseEntity<? extends BaseResponse> addCity(@RequestBody City city) {
        return createOrUpdateCity(city, false);
    }

    @PostMapping("/city/add-cities")
    public ResponseEntity<? extends BaseResponse> addCities(@RequestBody BaseListRequest<City> citiesRequest) {
        return createOrUpdateCities(citiesRequest.getData(), false);
    }

    @PutMapping("/city/update")
    public ResponseEntity<? extends BaseResponse> updateCity(@RequestBody City city) {
        return createOrUpdateCity(city, true);
    }

    @PutMapping("/city/update-cities")
    public ResponseEntity<? extends BaseResponse> updateCities(@RequestBody BaseListRequest<City> citiesRequest) {
        return createOrUpdateCities(citiesRequest.getData(), true);
    }

    @GetMapping("/city/list")
    public ResponseEntity<? extends BaseResponse> listCities() {
        final Long totalCount = cityRoutesService.countAllCities();
        final List<City> cities = cityRoutesService.findAllCities();
        BaseResponse response = ResponseFactory.createSuccessCollectionResponse(cities, totalCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/city/delete/{id}")
    public ResponseEntity<? extends BaseResponse> deleteCity(@PathVariable Long id) {
        cityRoutesService.deleteCity(id);
        return new ResponseEntity<>(ResponseFactory.createSuccessResponse(id), HttpStatus.OK);
    }

    @DeleteMapping("/city/name/delete/{cityName}")
    public ResponseEntity<? extends BaseResponse> deleteCity(@PathVariable String cityName) {
        cityRoutesService.deleteCityByName(cityName);
        return new ResponseEntity<>(ResponseFactory.createSuccessResponse(null), HttpStatus.OK);
    }

    @PostMapping("/route/define")
    public ResponseEntity<? extends BaseResponse> defineRoute(@RequestBody RouteDto route) {
        return createOrUpdateRoute(route, false);
    }

    @PutMapping("/route/define")
    public ResponseEntity<? extends BaseResponse> defineRoute(@RequestBody BaseListRequest<RouteDto> routeRequest) {
        return createOrUpdateRoutes(routeRequest.getData());
    }

    @GetMapping("/route/all")
    public ResponseEntity<? extends BaseResponse> listAllRoutes() {
        final List<RouteDto> routes = cityRoutesService.findRoutes();
        return new ResponseEntity<>(ResponseFactory.createSuccessCollectionResponse(
                routes, (long) routes.size()), HttpStatus.OK);
    }

    @PostMapping("/route/delete")
    public ResponseEntity<? extends BaseResponse> deleteRoutes(@RequestBody RouteDto route) {
        cityRoutesService.deleteRoutes(route);
        return new ResponseEntity<>(ResponseFactory.createSuccessResponse(null), HttpStatus.OK);
    }

    @PostMapping("/route/find")
    public ResponseEntity<? extends BaseResponse> findRoutes(@RequestBody RouteDto route) {
        final List<RouteDto> routes = cityRoutesService.findRoutes(route);
        return new ResponseEntity<>(
                ResponseFactory.createSuccessCollectionResponse(routes, (long) routes.size()), HttpStatus.OK);
    }

    @PostMapping("/route/find-feign")
    public ResponseEntity<List<RouteDto>> findRoutesFeign(@RequestBody RouteIdsRequestDto route) {
        final List<RouteDto> routes = cityRoutesService.findRoutes(route);
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    private ResponseEntity<? extends BaseResponse> createOrUpdateCities(List<City> cities, boolean update) {
        if (cities == null || cities.isEmpty()) {
            return new ResponseEntity<>(ResponseFactory.createSuccessResponse(null), HttpStatus.OK);
        }
        List<City> updatedCities = new LinkedList<City>();
        List<City> failedCities = new LinkedList<City>();

        cities.forEach(city -> {
            try {
                City newCity = cityRoutesService.createOrMergeCity(city);
                updatedCities.add(newCity);
            }
            catch (Exception e) {
                failedCities.add(city);
            }
        });
        HashMap<String, List<City>> result = new HashMap<>();
        result.put("updated", updatedCities);
        result.put("failed", failedCities);
        return new ResponseEntity<>(ResponseFactory.createSuccessResponse(result), HttpStatus.OK);
    }

    private ResponseEntity<? extends BaseResponse> createOrUpdateCity(City city, boolean update) {
        try {
            City newCity = cityRoutesService.createOrMergeCity(city);
            BaseResponse response = ResponseFactory.createSuccessResponse(update ? null : newCity.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    update
                            ? ResponseFactory.createErrorResponse(-2L, "Can not update the given city.")
                            : ResponseFactory.createErrorResponse(-1L, "Can not create the given city."),
                    HttpStatus.METHOD_FAILURE
            );
        }
    }

    private ResponseEntity<? extends BaseResponse> createOrUpdateRoute(RouteDto route, boolean update) {
        try {
            cityRoutesService.createOrUpdateRoute(route);
            return new ResponseEntity<>(ResponseFactory.createSuccessResponse(null), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(
                    update
                            ? ResponseFactory.createErrorResponse(-2L, "Can not update the given route.")
                            : ResponseFactory.createErrorResponse(-1L, "Can not create the given route."),
                    HttpStatus.METHOD_FAILURE
            );
        }
    }

    private ResponseEntity<? extends BaseResponse> createOrUpdateRoutes(List<RouteDto> routes) {
        if (routes == null || routes.isEmpty()) {
            return new ResponseEntity<>(ResponseFactory.createSuccessResponse(null), HttpStatus.OK);
        }
        List<RouteDto> updatedRoutes = new LinkedList<>();
        List<RouteDto> failedRoutes = new LinkedList<>();

        routes.forEach(route -> {
            if (cityRoutesService.createOrUpdateRoute(route)) {
                updatedRoutes.add(route);
            }
            else {
                failedRoutes.add(route);
            }
        });
        HashMap<String, List<RouteDto>> result = new HashMap<>();
        result.put("updated", updatedRoutes);
        result.put("failed", failedRoutes);
        return new ResponseEntity<>(ResponseFactory.createSuccessResponse(result), HttpStatus.OK);
    }
}
