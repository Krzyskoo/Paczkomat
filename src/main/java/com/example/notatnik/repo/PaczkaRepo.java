package com.example.notatnik.repo;

import com.example.notatnik.model.Paczka;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaczkaRepo extends JpaRepository<Paczka, Long> {
    @Query("SELECT p FROM Paczka p WHERE p.kodOdbioru = :kodOdbioru")
    Paczka findByKodOdbioru(String kodOdbioru);
    @Modifying
    @Query("DELETE FROM Paczka p WHERE p IN :paczki")
    void deletePaczki(@Param("paczki") List<Paczka> paczki);

    @Modifying
    @Query("DELETE FROM Paczka p WHERE p.id IN :paczkaIds")
    void deletePaczkiById(@Param("paczkaIds") List<Long> paczkaIds);

}
