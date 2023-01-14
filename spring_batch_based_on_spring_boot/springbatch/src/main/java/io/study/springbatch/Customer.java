package io.study.springbatch;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class Customer {
    private String name;
    private int age;
    private String year;
}
