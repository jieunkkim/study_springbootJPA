package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders") // @Table 로 name 지정하지 않으면, 알아서 class 명으로 table 명이 따짐
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // Foreign Key 설정
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList();
    /*
    Cascade Type 을 ALL 로 설정하면,
    Order 저장될 때 order 내 item 목록들도 알아서 OrderItem 에 저장해줌.
    delete 할 때도 마찬가지

    (before)    persist(itemA); persist(itemB); persist(itemC);   persist(order);
    (after)     persist(order);   (이후 itemA,B,C 는 알아서 OrderItem 에 저장)

     */

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]


    // 연관관계 편의 목적의 method
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    /*
    member 와 order 연관관계를 위해선 원래는 사용될 때 아래와 같이 짜야 함
    Member member = new Member();
    Order order = new Order();

    member.getOrders().add(order);      // 이 부분을 놏
    order.setMember(member);
     */

    // 연관관계 편의 목적의 method
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // 연관관계 편의 목적의 method
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== Order 를 생성하기 위한 메서드는 그냥 Domain 안에 구현==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { // ... 은 java 가변인자 로 검색해보면 됨
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
    //== 비즈니스 로직==//
    /**
      * 주문 취소
      */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소 불가합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
