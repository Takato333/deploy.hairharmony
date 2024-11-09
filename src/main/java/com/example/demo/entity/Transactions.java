package com.example.demo.entity;

import com.example.demo.entity.enums.TransactionsEnums;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

@Entity
@Data
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;

    Date createAt;

    @Enumerated(EnumType.STRING)
    TransactionsEnums status;
    String description;

    float amount;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "from_id")
    Account from;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "to_id")
    Account to;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "payment_id")
    Payment payment;


    @Override
    public String toString() {
        return "Transaction{id=" + id + ", createAt=" + createAt + ", status=" + status + ", description='" + description + "'}";
        // Omitting 'payment' to avoid recursion
    }


    @Override
    public int hashCode() {
        // Use fields that uniquely identify the Transaction
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Transactions)) return false;
        Transactions other = (Transactions) obj;
        return Objects.equals(createAt, other.createAt) && // Compare other relevant fields
                Objects.equals(from, other.from) &&
                Objects.equals(to, other.to) &&
                Objects.equals(status, other.status) &&
                Objects.equals(description, other.description);
    }

}
