package com.samha.persistence;

import com.samha.domain.Papel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPapelRepository extends JpaRepository<Papel, Long> {
    Papel findByNome(String nome);
}
