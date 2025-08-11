package com.proje.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proje.entity.Proje;
import com.proje.entity.ProjeDurumu;

@Repository
public interface ProjeRepository extends JpaRepository<Proje, Long> {
    
    List<Proje> findByDurum(ProjeDurumu durum);
    
    @Query("SELECT p FROM Proje p WHERE " +
           "LOWER(p.ad) LIKE LOWER(CONCAT('%', :arama, '%')) OR " +
           "LOWER(p.aciklama) LIKE LOWER(CONCAT('%', :arama, '%'))")
    List<Proje> findByAdOrAciklamaContainingIgnoreCase(@Param("arama") String arama);
    
    @Query("SELECT p FROM Proje p WHERE p.baslangicTarihi BETWEEN :baslangic AND :bitis")
    List<Proje> findByBaslangicTarihiBetween(@Param("baslangic") LocalDate baslangic, 
                                           @Param("bitis") LocalDate bitis);
    
    @Query("SELECT p FROM Proje p JOIN p.calisanlar c WHERE c.id = :calisanId")
    List<Proje> findProjelerByCalisan(@Param("calisanId") Long calisanId);
    
    @Query("SELECT COUNT(c) FROM Proje p JOIN p.calisanlar c WHERE p.id = :projeId")
    Long countCalisanlarByProje(@Param("projeId") Long projeId);
    
    @Query("SELECT p FROM Proje p WHERE p.bitisTarihi < :tarih AND p.durum != 'TAMAMLANDI'")
    List<Proje> findGecikmisProjeler(@Param("tarih") LocalDate tarih);
}
