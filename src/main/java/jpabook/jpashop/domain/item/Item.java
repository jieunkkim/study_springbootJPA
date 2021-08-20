package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   // Table 생성 전략 (movie, album, book 을 한 테이블로 만들기)
@DiscriminatorColumn(name = "dtype")    // Single Table 만들 때 album, book, movie 를 구분짓기 위해 dtype 컬럼 추가 (dtype value 는 각 하위 Entity class 에서 @DiscriminatorValue 로 정의해줌)
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity; // Setter 를 만들어두긴 했지만, stockQuantity 수량을 변경할 땐 직접 Setter 로 변경하지 않고, 비즈니스 로직을 활용할 것.

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //== 비즈니스 로직 ==//

    /**
     * stock 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int qunatity) {
        int restStock = this.stockQuantity - qunatity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity  = restStock;
    }
}
