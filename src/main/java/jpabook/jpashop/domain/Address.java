package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {      // 값타입 객체는 가능하면 Setter 두지말고 처음 생성 때만 값이 들어갈 수 있게 하자.

    private String city;
    private String street;
    private String zipcode;

    // JPA 를 사용하게 될 경우, Proxy 나 Reflection 를 자주 사용하게 되는데 이 때 비어있는 생성자를 필요로 한다
    // 따라서, 빈 생성자를 추가는 하되 굳이 public 으로 할 필요 없으니 protected 까지만 하게 제한을 두자
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
