package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        // OrderItem orderItem1 = new OrderItem();

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        //원래는 delivery save, orderItem save 등 해야하지만, CaseCadeType 속성으로 인해서 알아서 다 저장을 해준다?
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
        // JAP 활용 장점
        // 엔티티 안의 데이터를 변경하면 더티체킹, 변경내역감지가 일어나면서 database 에 update 쿼리가 알아서 날라간다. order.cancel()...
    }

    /**
     * 엔티티에 핵심 비지니스 로직을(OrderItem.createOrderItem(), Order.createOrder(), Order.cancel().... 작성하는 것을 도메인 모델 패던이라고 한다.
     * 서비스 계층은 단순히 엔티티에 필요한 요청을 위임만 한다.
     * 반대는 트랜잭션 스크립트 패턴이라고 한다.
     */

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

}
