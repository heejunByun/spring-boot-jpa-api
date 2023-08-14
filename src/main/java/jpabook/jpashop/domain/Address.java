package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;


// 주소 값 타입 (Getter 만 만듬)
// 변경이 되면 안됨 생성할 때만 값이 생성되어야함
@Embeddable // JAP 의 내장 타입이라는 것이기 때문에 어딘가에 내장될 수 있다.?
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // JPA 에서 protected 까지 허용해준다.ㄹ
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
