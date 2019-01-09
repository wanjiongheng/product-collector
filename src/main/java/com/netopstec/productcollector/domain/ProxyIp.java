package com.netopstec.productcollector.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * ProxyIp
 *
 * @author linyi
 * @date 2018/12/28 10:56
 */
@Data
@Entity
public class ProxyIp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;

    private Integer port;

    private String type;

    private String address;

    private Long reqMillisecond;

    private String lastValidation;
}
