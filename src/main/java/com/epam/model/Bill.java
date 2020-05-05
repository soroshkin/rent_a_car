package com.epam.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "bills")
@NamedQuery(name = Bill.GET, query = "SELECT b FROM Bill b WHERE id=:id")
@NamedQuery(name = Bill.DELETE, query = "DELETE FROM Bill b WHERE id=:id")
@NamedQuery(name = Bill.GET_ALL, query = "SELECT b FROM Bill b")
@NamedQuery(name = Bill.GET_BY_USER, query = "SELECT b FROM Bill b WHERE b.user=:user")
@NamedQuery(name = Bill.GET_BY_CAR, query = "SELECT b FROM Bill b WHERE b.car=:car")
@NamedQuery(name = Bill.EXISTS, query = "SELECT 1 FROM Bill b WHERE b.id=:id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Bill.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bill {
    public static final String GET = "Bill.findById";
    public static final String GET_ALL = "Bill.findAll";
    public static final String DELETE = "Bill.deleteById";
    public static final String GET_BY_USER = "Bill.findByUser";
    public static final String GET_BY_CAR = "Bill.findByCar";
    public static final String EXISTS = "Bill.exists";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PastOrPresent
    @NotNull
    private LocalDate date;

    @Column(scale = 2, precision = 10)
    @PositiveOrZero
    @NotNull
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    @NotNull
    private Car car;

    protected Bill() {
    }

    public Bill(@PastOrPresent @NotNull LocalDate date, @PositiveOrZero @NotNull BigDecimal amount, @NotNull User user, @NotNull Car car) {
        this.date = date;
        this.amount = amount;
        this.user = Objects.requireNonNull(user, "user must not be null");
        this.user.addBill(this);
        this.car = Objects.requireNonNull(car, "car must not be null");
        this.car.addBill(this);
    }

    @PreRemove
    public void deleteCar() {
        car.removeBill(this);
    }

    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return date.equals(bill.date) &&
                amount.compareTo(bill.amount) == 0 &&
                user.equals(bill.user) &&
                car.equals(bill.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, user, car);
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
}
