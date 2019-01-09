package com.netopstec.productcollector.repository;

import com.netopstec.productcollector.domain.ProxyIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * ProxyIpRepository
 *
 * @author linyi
 * @date 2018/12/28 10:56
 */
@Repository
public interface ProxyIpRepository extends JpaRepository<ProxyIp, Long> {

}
