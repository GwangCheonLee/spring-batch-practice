package com.example.springbatchpractice.another.repositories;

import com.example.springbatchpractice.another.entities.Another;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnotherRepository extends JpaRepository<Another, Long> {
}
