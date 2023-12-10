package com.example.loanapp.repo;

import com.example.loanapp.model.ParcelLocker;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelLockerRepo extends JpaRepository<ParcelLocker, Long> {
    @Query("SELECT pl FROM ParcelLocker pl WHERE pl.isFree = true")
    List<ParcelLocker> findAllFreeParcelLockers();

    @Modifying
    @Transactional
    @Query("UPDATE ParcelLocker e SET e.isFree = :newStatus WHERE e.id = :entityId")
    void updateStatus(@Param("entityId") Long entityId, @Param("newStatus") boolean newStatus);
}
