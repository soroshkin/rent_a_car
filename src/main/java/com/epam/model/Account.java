package com.epam.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Entity
@Table(name = "accounts")
@NamedQuery(name = Account.GET, query = "SELECT a FROM Account a WHERE id=:id")
@NamedQuery(name = Account.DELETE, query = "DELETE FROM Account a WHERE id=:id")
@NamedQuery(name = Account.GET_ALL, query = "SELECT a FROM Account a")
public class Account {
    public static final String GET = "Account.get";
    public static final String GET_ALL = "Account.getAll";
    public static final String DELETE = "Account.delete";

    @Id
    private Long id;

    @Column(name = "deposint_USD", scale = 2, precision = 12)
    @NotNull
    private BigDecimal depositUSD;

    @Column(name = "deposit_EUR", scale = 2, precision = 12)
    @NotNull
    private BigDecimal depositEUR;

    @OneToOne
    @MapsId
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    protected Account() {
    }

    public Account(User user) {
        MathContext mathContext = new MathContext(2, RoundingMode.HALF_EVEN);
        this.depositUSD = new BigDecimal(0, mathContext);
        this.depositEUR = new BigDecimal(0, mathContext);
        this.user = user;
    }

    public BigDecimal getDepositUSD() {
        return depositUSD;
    }

    public void setDepositUSD(BigDecimal depositUSD) {
        this.depositUSD = depositUSD;
    }

    public BigDecimal getDepositEUR() {
        return depositEUR;
    }

    public void setDepositEUR(BigDecimal depositEUR) {
        this.depositEUR = depositEUR;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", depositUSD=" + depositUSD +
                ", depositEUR=" + depositEUR +
                '}';
    }
}