package com.proje.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "projeler")
public class Proje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Proje adı boş olamaz")
    @Size(min = 3, max = 100, message = "Proje adı 3-100 karakter arasında olmalıdır")
    @Column(nullable = false, length = 100)
    private String ad;
    
    @Size(max = 500, message = "Açıklama en fazla 500 karakter olabilir")
    @Column(length = 500)
    private String aciklama;
    
    @Column(name = "baslangic_tarihi")
    private LocalDate baslangicTarihi;
    
    @Column(name = "bitis_tarihi")
    private LocalDate bitisTarihi;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjeDurumu durum = ProjeDurumu.YENI;
    
    @Column(name = "olusturma_tarihi", nullable = false, updatable = false)
    private LocalDateTime olusturmaTarihi;
    
    @Column(name = "guncelleme_tarihi")
    private LocalDateTime guncellemeTarihi;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "proje_calisan",
        joinColumns = @JoinColumn(name = "proje_id"),
        inverseJoinColumns = @JoinColumn(name = "calisan_id")
    )
    @JsonIgnoreProperties("calisanlar")
    private Set<Calisan> calisanlar = new HashSet<>();
    
    // Constructors
    public Proje() {}
    
    public Proje(String ad, String aciklama, LocalDate baslangicTarihi, LocalDate bitisTarihi) {
        this.ad = ad;
        this.aciklama = aciklama;
        this.baslangicTarihi = baslangicTarihi;
        this.bitisTarihi = bitisTarihi;
    }
    
    @PrePersist
    protected void onCreate() {
        olusturmaTarihi = LocalDateTime.now();
        guncellemeTarihi = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        guncellemeTarihi = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAd() {
        return ad;
    }
    
    public void setAd(String ad) {
        this.ad = ad;
    }
    
    public String getAciklama() {
        return aciklama;
    }
    
    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
    
    public LocalDate getBaslangicTarihi() {
        return baslangicTarihi;
    }
    
    public void setBaslangicTarihi(LocalDate baslangicTarihi) {
        this.baslangicTarihi = baslangicTarihi;
    }
    
    public LocalDate getBitisTarihi() {
        return bitisTarihi;
    }
    
    public void setBitisTarihi(LocalDate bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }
    
    public ProjeDurumu getDurum() {
        return durum;
    }
    
    public void setDurum(ProjeDurumu durum) {
        this.durum = durum;
    }
    
    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }
    
    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }
    
    public LocalDateTime getGuncellemeTarihi() {
        return guncellemeTarihi;
    }
    
    public void setGuncellemeTarihi(LocalDateTime guncellemeTarihi) {
        this.guncellemeTarihi = guncellemeTarihi;
    }
    
    public Set<Calisan> getCalisanlar() {
        return calisanlar;
    }
    
    public void setCalisanlar(Set<Calisan> calisanlar) {
        this.calisanlar = calisanlar;
    }
    
    // Helper methods
    public void addCalisan(Calisan calisan) {
        this.calisanlar.add(calisan);
        calisan.getProjeler().add(this);
    }
    
    public void removeCalisan(Calisan calisan) {
        this.calisanlar.remove(calisan);
        calisan.getProjeler().remove(this);
    }
}