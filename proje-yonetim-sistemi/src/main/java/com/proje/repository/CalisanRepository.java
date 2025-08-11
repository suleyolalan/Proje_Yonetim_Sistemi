package com.proje.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proje.entity.Calisan;

@Repository
public interface CalisanRepository extends JpaRepository<Calisan, Long> {
    
    Optional<Calisan> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT c FROM Calisan c WHERE " +
           "LOWER(c.ad) LIKE LOWER(CONCAT('%', :arama, '%')) OR " +
           "LOWER(c.soyad) LIKE LOWER(CONCAT('%', :arama, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :arama, '%'))")
    List<Calisan> findByAdOrSoyadOrEmailContainingIgnoreCase(@Param("arama") String arama);
    
    List<Calisan> findByPozisyon(String pozisyon);
    
    @Query("SELECT c FROM Calisan c WHERE c.id NOT IN " +
           "(SELECT pc.id FROM Proje p JOIN p.calisanlar pc WHERE p.id = :projeId)")
    List<Calisan> findCalisanlarNotInProje(@Param("projeId") Long projeId);
    
    @Query("SELECT COUNT(p) FROM Calisan c JOIN c.projeler p WHERE c.id = :calisanId")
    Long countProjelerByCalisan(@Param("calisanId") Long calisanId);
}