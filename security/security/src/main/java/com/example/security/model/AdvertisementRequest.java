package com.example.security.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="ADVERTISEMENT_REQUEST")
public class AdvertisementRequest {

    private static final long serialVersionUId =  1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clientid", referencedColumnName = "id")
    private Client client;

    @Column(name = "expirationTime")
    private Timestamp expirationTime;

    @Column(name = "activityStart")
    private Timestamp activityStart;

    @Column(name = "activityEnd")
    private Timestamp activityEnd;

    @Column(name="requestStatus")
    private RequestStatus requestStatus;

    public AdvertisementRequest() {}

    public AdvertisementRequest(Long id, String description, Client client, Timestamp expirationTime, Timestamp activityStart, Timestamp activityEnd, RequestStatus status) {
        this.id = id;
        this.description = description;
        this.client = client;
        this.expirationTime = expirationTime;
        this.activityStart = activityStart;
        this.activityEnd = activityEnd;
        this.requestStatus = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Timestamp getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Timestamp expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Timestamp getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(Timestamp activityStart) {
        this.activityStart = activityStart;
    }

    public Timestamp getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(Timestamp activityEnd) {
        this.activityEnd = activityEnd;
    }
}
