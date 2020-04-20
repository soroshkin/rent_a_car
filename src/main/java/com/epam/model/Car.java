package com.epam.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "cars")
@NamedQuery(name = Car.GET, query = "SELECT c FROM Car c WHERE id=:id")
@NamedQuery(name = Car.DELETE, query = "DELETE FROM Car c WHERE id=:id")
@NamedQuery(name = Car.GET_ALL, query = "SELECT c FROM Car c")
public class Car {
    public static final String GET = "Car.get";
    public static final String GET_ALL = "Car.getAll";
    public static final String DELETE = "Car.delete";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @NotNull
    private String model;

    @Column(name = "production_date")
    @NotNull
    @Past
    private LocalDate productionDate;

    @PositiveOrZero
    private int mileage;

    @ManyToMany(mappedBy = "tripsByCar")
    private Set<User> users;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bill> bills;

    protected Car() {
    }

    public Car(String model, LocalDate productionDate, @PositiveOrZero int mileage) {
        this.model = model;
        this.productionDate = productionDate;
        this.mileage = mileage;
    }

    public Long getId() {
        return id;
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

    public Set<Bill> getBills() {
        return bills;
    }

    public boolean addBill(Bill bill) {
        return bills.add(bill);
    }

    public boolean isNew() {
        return this.id == null;
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
