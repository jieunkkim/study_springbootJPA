package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id") // @Column 으로 name 따로 정의 안하면, field 명으로 column 명 따진다.
    private Long id;

    private String name;

    @Embedded
    private Address address;  // Address 위에 @Embedable 적혀있으면 생략해도 됨

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
