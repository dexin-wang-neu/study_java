package com.atguigu.springcloud.entities_out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 因为多个模块都用到了该类，工程重构后将该类放在cloud-api-commons中，本类废弃
 * @param <T>
 */
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class CommonResult<T> {
//    //404 not_found
//    private Integer code;
//    private String  message;
//    private T       data;
//
//    public CommonResult(Integer code,String message){
//        this(code,message,null);
//    }
//}
