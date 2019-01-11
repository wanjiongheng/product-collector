package com.netopstec.productcollector.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * JdKefir
 *
 * @author linyi
 * @date 2019/1/11 16:51
 */
@Data
@Entity
public class JdKefir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vender;

    private String deliver;

    private String weight;

    private String stockDesc;

    private BigDecimal price;
}
