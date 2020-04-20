package com.epam.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "passports")
@NamedQuery(name = Passport.GET, query = "SELECT p FROM Passport p WHERE id=:id")
@NamedQuery(name = Passport.GET_ALL, query = "SELECT p FROM Passport p")
@NamedQuery(name = Passport.DELETE, query = "DELETE FROM Passport p WHERE id=:id")
public class Passport {
    public static final String GET = "Passport.get";
    public static final String GET_ALL = "Passport.getAll";
    public static final String DELETE = "Passport.delete";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "passport_number", unique = true)
    @NotNull
    @NotBlank
    private String passportNumber;

    @NotNull
    @NotBlank
    private String address;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String surname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    protected Passport() {
    }

    public Passport(String passportNumber, String address, String name, String surname, User user) {
        this.passportNumber = passportNumber;
        this.address = address;
        this.name = name;
        this.surname = surname;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return "Passport{" +
                "passportNumber='" + passportNumber + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
