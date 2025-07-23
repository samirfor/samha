package com.samha.persistence;

import com.samha.domain.Eixo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEixoRepository extends JpaRepository<Eixo, Long> {
}
