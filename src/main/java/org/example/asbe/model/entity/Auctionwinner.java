package org.example.asbe.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "auctionwinners")
public class Auctionwinner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('auctionwinners_winner_id_seq')")
    @Column(name = "winner_id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Auctionitem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Userinfo user;

    @NotNull
    @Column(name = "winning_bid", nullable = false, precision = 10, scale = 2)
    private BigDecimal winningBid;

    @Size(max = 20)
    @ColumnDefault("'pending'")
    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    @Size(max = 20)
    @ColumnDefault("'pending'")
    @Column(name = "delivery_status", length = 20)
    private String deliveryStatus;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}