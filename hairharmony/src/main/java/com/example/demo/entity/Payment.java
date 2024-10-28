package com.example.demo.entity;


import com.example.demo.entity.enums.PaymentEnums;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Date createAt;

    @Enumerated(EnumType.STRING)
    PaymentEnums method;

    float total;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")// payment chỉ tạo ra khi order đc thành công . đặt key ở payment
    Appointment appointment;

    @OneToMany(mappedBy = "payment",cascade = CascadeType.ALL)
    Set<Transactions> transactions = new HashSet<>();



    @Override
    public String toString() {
        return "Payment{id=" + id + ", createAt=" + createAt + ", method=" + method + "}";
        // Omitting 'transactions' to avoid recursion
    }
    @Override
    public int hashCode() {
        // Use fields that uniquely identify the Payment
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Payment)) return false;
        Payment other = (Payment) obj;
        return Objects.equals(id, other.id); // Compare relevant fields
    }


}