package com.proje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "calisanlar")
public class Calisan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Ad alanı boş olamaz")
    @Size(min = 2, max = 50, message = "Ad 2-50 karakter arasında olmalıdır")
    @Column(nullable = false, length = 50)
    private String ad;
    
    @NotBlank(message = "Soyad alanı boş olamaz")
    @Size(min = 2, max = 50, message = "Soyad 2-50 karakter arasında olmalıdır")
    @Column(nullable = false, length = 50)
    private String soyad;
    
    @NotBlank(message = "Email alanı boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    @Column(nullable = false, unique = true)
    private String email;
    
    @Size(max = 100, message = "Pozisyon en fazla 100 karakter olabilir")
    @Column(length = 100)
    private String pozisyon;
    
    @Column(name = "olusturma_tarihi", nullable = false, updatable = false)
    private LocalDateTime olusturmaTarihi;
    
    @Column(name = "guncelleme_tarihi")
    private LocalDateTime guncellemeTarihi;
    
    @ManyToMany(mappedBy = "calisanlar", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("calisanlar")
    private Set<Proje> projeler = new HashSet<>();
    
    // Constructors
    public Calisan() {}
    
    public Calisan(String ad, String soyad, String email, String pozisyon) {
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.pozisyon = pozisyon;
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
    
    public String getSoyad() {
        return soyad;
    }
    
    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPozisyon() {
        return pozisyon;
    }
    
    public void setPozisyon(String pozisyon) {
        this.pozisyon = pozisyon;
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
    
    public Set<Proje> getProjeler() {
        return projeler;
    }
    
    public void setProjeler(Set<Proje> projeler) {
        this.projeler = projeler;
    }
    
    public String getTamAd() {
        return ad + " " + soyad;
    }
}