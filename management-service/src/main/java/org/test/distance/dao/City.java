package org.test.distance.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Entity class of the city
 * @author Viktar Lebedzeu
 */
@Entity
@Table(name = "city")
public class City implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 250, nullable = false)
    private String name;

    @Column(name = "country", length = 50, nullable = true)
    private String country;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cityFrom")
    @JsonIgnore
    private List<Route> outgoingRoutes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cityTo")
    @JsonIgnore
    private List<Route> incomingRoutes;

    public City() {
    }

    public City(Long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Route> getOutgoingRoutes() {
        return outgoingRoutes;
    }

    public void setOutgoingRoutes(List<Route> outgoingRoutes) {
        this.outgoingRoutes = outgoingRoutes;
    }

    public List<Route> getIncomingRoutes() {
        return incomingRoutes;
    }

    public void setIncomingRoutes(List<Route> incomingRoutes) {
        this.incomingRoutes = incomingRoutes;
    }
}
