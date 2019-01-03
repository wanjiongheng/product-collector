package com.netopstec.productcollector.repository;

import com.netopstec.productcollector.domain.TmallKefir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TmallKefirRepository
 *
 * @author linyi
 * @date 2019/1/3 15:24
 */
@Repository
public interface TmallKefirRepository extends JpaRepository<TmallKefir, Long> {
}
