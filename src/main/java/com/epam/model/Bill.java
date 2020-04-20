package com.epam.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    public Bill(@PastOrPresent LocalDate date, @PositiveOrZero BigDecimal amount, User user, Car car) {
        this.date = date;
        this.amount = amount;
        this.user = user;
        this.car = car;
    }

    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isNew() {
        return id == null;
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
