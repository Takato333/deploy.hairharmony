package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Data
public class Stylist {
    @Column(unique = true) // không được trùng
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;

    @NotBlank(message = "Name cannot blank!")
    String fullName;

    String image;

    String sex;

    String description;

    int level;

    @JsonIgnore//không bắt nhập thông tin trên swagger
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

//    @OneToMany(mappedBy = "shiftStylist")
//    @JsonIgnore
//    List<Shift> shifts;

    @OneToMany(mappedBy = "slotStylist")
    @JsonIgnore
    List<Slot> slots;


    @OneToMany(mappedBy = "stylist")
    @JsonIgnore
    List<AppointmentDetail> appointmentDetails;

    @Override
    public String toString() {
        return "Stylist{id=" + id + ", fullName=" + fullName + "}";  // Keep it simple
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Stylist)) return false;
        Stylist other = (Stylist) obj;
        return Objects.equals(id, other.id); // Compare relevant fields
    }


}
