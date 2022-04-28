package org.raavi.demo.repository;

import org.raavi.demo.dao.NaceDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaceRepo extends JpaRepository<NaceDAO, Long> {
    NaceDAO findByOrder(int order);
}
