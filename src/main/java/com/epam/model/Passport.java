package com.epam.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "passports")
@NamedQuery(name = Passport.FIND_BY_ID, query = "SELECT p FROM Passport p WHERE id=:id")
@NamedQuery(name = Passport.FIND_ALL, query = "SELECT p FROM Passport p join fetch p.user")
@NamedQuery(name = Passport.FIND_BY_USER, query = "SELECT p FROM Passport p WHERE p.user.id=:userId")
@NamedQuery(name = Passport.DELETE, query = "DELETE FROM Passport p WHERE id=:id")
@NamedQuery(name = Passport.EXISTS, query = "SELECT 1 FROM Passport p WHERE p.id=:id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Passport.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedEntityGraph(name = Passport.GRAPH_PASSPORT_USER, attributeNodes = @NamedAttributeNode("user"))
public class Passport {
    public static final String FIND_BY_ID = "Passport.findById";
    public static final String FIND_ALL = "Passport.findAll";
    public static final String DELETE = "Passport.deleteById";
    public static final String FIND_BY_USER = "Passport.findByUser";
    public static final String EXISTS = "Passport.exists";
    public static final String GRAPH_PASSPORT_USER = "graph.Passport.user";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    protected Passport() {
    }

    public Passport(@NotNull @NotBlank String passportNumber, @NotNull @NotBlank String address, @NotNull @NotBlank String name, @NotNull @NotBlank String surname, @NotNull User user) {
        this.passportNumber = passportNumber;
        this.address = address;
        this.name = name;
        this.surname = surname;
        this.user = Objects.requireNonNull(user, "user must not be null");
        this.user.addPassport(this);
    }

    @JsonIgnore
    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passport passport = (Passport) o;
        return passportNumber.equals(passport.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber);
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
