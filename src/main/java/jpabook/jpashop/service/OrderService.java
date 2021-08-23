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
    public Long order(Long memberId, Long itemId, int count){

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        /*
            order 를 Repository 사용해서 저장하는 것 처럼
            delivery 와 orderItem 도 각각의 Repository 를 사용하여 저장해줘야 하지 않나?
            => Order Entity 의 orderItem, delivery 에 걸려있는 cascade 옵션으로 알아서 데이터 넣어준다 (JPA)
         */
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     * (JPA의 강점. 만약 아래를 SQL 방식으로 진행했다면 order , orderItem, Item 의 값 변경 시 매 Step update SQL 을 쏴줘야 했겠지만
     *  JPA의 경우 아래와 같이 비즈니스 로직 처리로 Entity 값 변경을 해주면 변경 포인트들을 찾아서 알아서 update 해준다..? dirty checking?)
     */
    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
