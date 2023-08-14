package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
    // 상품은 책만 함

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;


}
