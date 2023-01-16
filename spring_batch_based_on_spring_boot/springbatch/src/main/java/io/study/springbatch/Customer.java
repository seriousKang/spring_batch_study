package io.study.springbatch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Customer {
    private Long id;
    private String name;
    private int age;
}
