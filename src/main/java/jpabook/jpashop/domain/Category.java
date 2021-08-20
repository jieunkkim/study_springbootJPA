package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name="category_id"),
            inverseJoinColumns = @JoinColumn(name="item_id"))  // 다대다 Entity 관계를 표현하려면 관계형 DB에선 중간테이블이 필요
    private List<Item> items = new ArrayList<>();

    // 아래 두 필드는 Category 계층 관계를 표현 시 사용. category 안에서 서로 참조..?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // 연관관계 편의 method
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }

}
