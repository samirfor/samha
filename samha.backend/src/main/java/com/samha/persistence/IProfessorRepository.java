package com.samha.persistence;

import com.samha.domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProfessorRepository extends JpaRepository<Professor, Long> {
}
