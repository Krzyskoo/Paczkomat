package com.example.loanapp.repo;

import com.example.loanapp.model.Packs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PacksRepo extends JpaRepository<Packs, Long> {

    List<Packs> findByExpirationDateLessThan(LocalDateTime plusDays);

    @Query("SELECT p FROM Packs p WHERE p.pickupCode = :pickupCode")
    Packs findByPickupCode(@Param("pickupCode") String pickupCode);
}
