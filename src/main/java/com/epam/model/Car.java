package com.epam.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cars")
@NamedQuery(name = Car.GET, query = "SELECT c FROM Car c WHERE id=:id")
@NamedQuery(name = Car.DELETE, query = "DELETE FROM Car c WHERE id=:id")
@NamedQuery(name = Car.GET_ALL, query = "SELECT c FROM Car c")
@NamedQuery(name = Car.EXISTS, query = "SELECT 1 FROM Car c WHERE c.id=:id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Car.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {
    public static final String GET = "Car.findById";
    public static final String GET_ALL = "Car.findAll";
    public static final String DELETE = "Car.deleteById";
    public static final String EXISTS = "Car.exists";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    private String model;

    @NotBlank
    @NotNull
    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    @Column(name = "production_date")
    @NotNull
    @Past
    private LocalDate productionDate;

    @PositiveOrZero
    private int mileage;

    @ManyToMany(mappedBy = "tripsByCar")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Bill> bills = new HashSet<>();

    protected Car() {
    }

    public Car(@NotBlank @NotNull String model, @NotBlank @NotNull String registrationNumber, @NotNull @Past LocalDate productionDate, @PositiveOrZero int mileage) {
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.productionDate = productionDate;
        this.mileage = mileage;
    }

    public Long getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getModel() {
        return model;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public boolean addBill(Bill bill) {
        bill.setCar(this);
        return bills.add(bill);
    }

    public void removeBill(Bill bill) {
        bills.remove(bill);
        bill.setCar(null);
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return registrationNumber.equals(car.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", productionYear=" + productionDate +
                ", mileage=" + mileage +
                '}';
    }
}
