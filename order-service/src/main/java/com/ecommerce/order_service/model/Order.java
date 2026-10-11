package com.ecommerce.order_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // orphanRemoval is key for updates
    @JoinColumn(name = "order_id") // creates a FK on the items table
    private List<OrderLineItems> orderLineItemsList;
}