package com.github.springbootproject.repository.storeSales;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Builder
@NoArgsConstructor
@Entity
@Table(name = "store_sales")
public class StoreSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "store_name", length = 30)
    private String storeName;

    @Column(name = "amount", columnDefinition = "DEFAULT 0 CHECK(amount) >= 0", nullable = false)
    private Integer amount;
}
