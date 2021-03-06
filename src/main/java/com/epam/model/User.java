package com.epam.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NamedQuery(name = User.GET, query = "SELECT u FROM User u WHERE id=:id")
@NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE id=:id")
@NamedQuery(name = User.GET_ALL, query = "SELECT u FROM User u")
public class User {
    public static final String GET = "User.get";
    public static final String GET_ALL = "User.getAll";
    public static final String DELETE = "User.delete";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    @Email
    @NotNull
    @NotBlank
    private String email;

    @Column(name = "date_of_birth")
    @Past
    @NotNull
    private LocalDate dateOfBirth;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Account account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Passport> passports;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bill> bills;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "trips",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = @JoinColumn(name = "cars_id"))
    private Set<Car> tripsByCar;

    protected User() {
    }

    public User(String email, LocalDate dateOfBirth) {
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.account = new Account(this);
        this.bills = new HashSet<>();
        this.passports = new HashSet<>();
        this.tripsByCar = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public Set<Car> getTripsByCar() {
        return tripsByCar;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<Passport> getPassports() {
        return passports;
    }

    public boolean addPassport(Passport passport) {
        passport.setUser(this);
        return passports.add(passport);
    }

    public boolean removePassport(Passport passport) {
        return passports.remove(passport);
    }

    public boolean addBill(Bill bill) {
        bill.setUser(this);
        return bills.add(bill);
    }

    public boolean removeBill(Bill bill) {
        return bills.remove(bill);
    }

    public boolean addTripsByCar(Car car) {
        return tripsByCar.add(car);
    }

    public boolean removeTripsByCar(Car car) {
        return tripsByCar.remove(car);
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", account=" + account +
                ", passports=" + passports +
                ", bills=" + bills +
                ", tripsByCar=" + tripsByCar +
                '}';
    }
}
