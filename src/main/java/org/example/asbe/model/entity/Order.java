package org.example.asbe.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('orders_order_id_seq')")
    @Column(name = "order_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Userinfo user;

    @NotNull
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "shipping_address", nullable = false, length = Integer.MAX_VALUE)
    private String shippingAddress;

    @Size(max = 15)
    @NotNull
    @Column(name = "shipping_phone", nullable = false, length = 15)
    private String shippingPhone;

    @Size(max = 100)
    @NotNull
    @Column(name = "shipping_name", nullable = false, length = 100)
    private String shippingName;

    @Size(max = 20)
    @ColumnDefault("'pending'")
    @Column(name = "order_status", length = 20)
    private String orderStatus;

    @Size(max = 50)
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Size(max = 20)
    @ColumnDefault("'unpaid'")
    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

//    @OneToMany(mappedBy = "book_id")
//    private List<Book> books;

    @OneToMany(mappedBy = "order")
    private Set<Orderitem> orderItems = new LinkedHashSet<>();


}