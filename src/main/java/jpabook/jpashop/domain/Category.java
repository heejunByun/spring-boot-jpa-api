package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    // 중간 테이블 매핑 (다대다 = <1대다 - 다대1>) // 외래키 이외의 칼럼을 추가할 수 없기 때문에 실무에서는 사용할 수 없다.
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"), // 중간 테이블 category_item 에 있는 category_id
            inverseJoinColumns =  @JoinColumn(name = "item_id") // 중간 테이블 category_item 에 있는 item_id
    )
    private List<Item> items = new ArrayList<>();

    // 셀프로 양방향 관계관계를 걸어준 것 > 이해가 잘 안됨
    // 부모
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 자식 : 자식은 여러개
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //===연관관계 편의 메서드===//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
