package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    // EnumType 에는 ORDINAL, STRING 이 있는데 ORDINAL 사용 시 운영하다가 Enum 이 추가될 경우 추가를 할 수 없다.
    // 꼭 EnumType 은 STRING 으로 써야한다.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // [READY, COMP]


}
