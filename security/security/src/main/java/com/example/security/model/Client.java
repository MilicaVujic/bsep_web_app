package com.example.security.model;

import javax.persistence.*;

@Entity
@EntityListeners(ClientListener.class)
@Table(name="CLIENTS")
public class Client {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phone;

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    @Column(name = "pib")
    private String pib;

    @Column(name = "type")
    private ClientType type;
    @Column(name = "packageType")
    private PackageType packageType;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPib() {
        return pib;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public Client(){}

    public Client(Long id, String phone, String pib, ClientType type, User user) {
        this.id = id;
        this.phone = phone;
        this.pib = pib;
        this.type = type;
        this.user=user;
        this.packageType=packageType;
    }
}
