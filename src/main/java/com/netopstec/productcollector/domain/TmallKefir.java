package com.netopstec.productcollector.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TmallKefir
 *
 * @author linyi
 * @date 2019/1/3 15:20
 */
@Data
@Entity
public class TmallKefir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;

    private String type;

    private Date createTime;

    private Date modifyTime;

    private Integer flag;
}
