package com.example.security.model;

import javax.persistence.*;

@Entity
@Table(name="ADVERTISEMENT")
public class Advertisement {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slogan")
    private String slogan;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "clientid", referencedColumnName = "id")
    private Client client;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private double duration;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requestId", referencedColumnName = "id")
    private AdvertisementRequest request;

    public Advertisement() {}

    public Advertisement(Long id, String slogan, Client client, String description, double duration, AdvertisementRequest request) {
        this.id = id;
        this.slogan = slogan;
        this.client = client;
        this.description = description;
        this.duration = duration;
        this.request = request;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public AdvertisementRequest getRequest() {
        return request;
    }

    public void setRequest(AdvertisementRequest request) {
        this.request = request;
    }
}
