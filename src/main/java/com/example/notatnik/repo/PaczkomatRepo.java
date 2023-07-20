package com.example.notatnik.repo;

import com.example.notatnik.model.Paczka;
import com.example.notatnik.model.Paczkomat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaczkomatRepo extends JpaRepository<Paczkomat, Long> {
    List<Paczkomat> findByStatus(String status);
    @Query("SELECT p.paczkomat.numer from Paczka p WHERE p.id=:paczkaId")
    Integer findPaczkomatByIdPaczki(@Param("paczkaId") Long paczkaId);
    @Modifying
    @Query("UPDATE Paczkomat p SET p.status = 'wolna' WHERE p.id IN :paczkomatIds")
    void updatePaczkomatStatus(@Param("paczkomatIds") List<Long> paczkomatIds);


}
