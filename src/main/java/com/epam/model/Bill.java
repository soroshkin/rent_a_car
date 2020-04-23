package com.epam.model;

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
public class Bill {
    public static final String GET = "Bill.get";
    public static final String GET_ALL = "Bill.getAll";
    public static final String DELETE = "Bill.delete";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
