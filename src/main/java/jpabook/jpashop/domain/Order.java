package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //Order 와 Member 는 다대1의 관계이다 여러개의 주문을 하나의 회원이 할 수 있다.
    //Order 와 Member 의 관계중 Order 에 member_id 의 외래키가 가깝게 있기 때문에, 연관관계의 주인은 Order 가 된다.
    //연관관계의 주인은 추가적인 정보를 입력할 필요가 없다.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id") // 외래키의 칼럼이 member_id 가 된다.
    private Member member;

    // cascade = CascadeType.ALL) : orderItems 에다가 데이터를 넣어두고 데이터를 저장하면, Order 에도 저장이된다. > persist 전파한다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // Java 8

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    //===연관관계 편의 메서드===//
    // 메서드 하나에서 양방향 연관관계를 넣어준다.
    // member 에도 들어가고 order 에도 들어가는 함수
    // 메서드 위치는 핵심적으로 컨트롤 하는쪽에서 들고있는게 좋다.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //===생성 메서드===//
    // 복잡한 생성은 별도의 생성 메서드가 있으면 좋다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { // ... 여러개를 넘기겠다는 의미
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비지니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
