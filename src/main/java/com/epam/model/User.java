package com.epam.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@NamedQuery(name = User.GET, query = "SELECT u FROM User u WHERE id=:id")
@NamedQuery(name = User.GET_BY_EMAIL, query = "SELECT u FROM User u WHERE email=:email")
@NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE id=:id")
@NamedQuery(name = User.GET_ALL, query = "SELECT u FROM User u")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Long.class)
public class User {
    public static final String GET = "User.get";
    public static final String GET_BY_EMAIL = "User.getByEmail";
    public static final String GET_ALL = "User.getAll";
    public static final String DELETE = "User.delete";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "email", unique = true)
    @Email
    @NotNull
    @NotBlank
    private String email;

    @Column(name = "date_of_birth")
    @Past
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Account account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Passport> passports = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bill> bills = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "trips",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = @JoinColumn(name = "cars_id"))
    private Set<Car> tripsByCar = new HashSet<>();

    protected User() {
    }

    public User(String email, LocalDate dateOfBirth) {
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.account = new Account(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public void setEmail(String email) {
        this.email = email;
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

    public void setPassports(Set<Passport> passports) {
        this.passports = passports;
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

    @JsonIgnore
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
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
