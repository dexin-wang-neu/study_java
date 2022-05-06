package com.atguigu.springcloud.dao;


import com.atguigu.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface PaymentDao {
    /** 建表sql
         CREATE TABLE payment(
         id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
         serial VARCHAR(200) DEFAULT '',
         PRIMARY KEY(id)
         )
         ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET = utf8;
     *
     */
    public int create(Payment payment);

    public Payment getPaymentById(@Param("id") Long id);
}
