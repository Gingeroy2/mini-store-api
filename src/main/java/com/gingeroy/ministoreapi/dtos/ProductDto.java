package com.gingeroy.ministoreapi.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    //    在这个类中，添加想要对外暴露的字段

//    @JsonIgnore - 排除 json 对象中的某个字段
//    @JsonProperty("xx") - 指定 json 对象中的字段名称，相当于“重命名”
//    @JsonInclude(JsonInclude.Include.NON_NULL) - 排除 json 对象中的 null 值

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}
