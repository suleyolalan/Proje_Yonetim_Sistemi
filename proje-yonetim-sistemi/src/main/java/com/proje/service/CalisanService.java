package com.proje.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proje.entity.Calisan;
import com.proje.repository.CalisanRepository;

@Service
@Transactional
public class CalisanService {
    
    @Autowired
    private CalisanRepository calisanRepository;
    
    public List<Calisan> tumCalisanlariGetir() {
        return calisanRepository.findAll();
    }
    
    public Optional<Calisan> calisanGetir(Long id) {
        return calisanRepository.findById(id);
    }
    
    public Optional<Calisan> emailIleCalisanGetir(String email) {
        return calisanRepository.findByEmail(email);
    }
    
    public Calisan calisanKaydet(Calisan calisan) {
        if (calisanRepository.existsByEmail(calisan.getEmail())) {
            throw new RuntimeException("Bu email adresi zaten kullanılıyor: " + calisan.getEmail());
        }
        try {
            return calisanRepository.save(calisan);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Çalışan kaydedilemedi. Email adresi benzersiz olmalıdır.");
        }
    }
    
    public Calisan calisanGuncelle(Long id, Calisan guncelCalisan) {
        return calisanRepository.findById(id)
            .map(mevcutCalisan -> {
                // Email değişikliği kontrolü
                if (!mevcutCalisan.getEmail().equals(guncelCalisan.getEmail()) && 
                    calisanRepository.existsByEmail(guncelCalisan.getEmail())) {
                    throw new RuntimeException("Bu email adresi zaten kullanılıyor: " + guncelCalisan.getEmail());
                }
                
                mevcutCalisan.setAd(guncelCalisan.getAd());
                mevcutCalisan.setSoyad(guncelCalisan.getSoyad());
                mevcutCalisan.setEmail(guncelCalisan.getEmail());
                mevcutCalisan.setPozisyon(guncelCalisan.getPozisyon());
                
                return calisanRepository.save(mevcutCalisan);
            })
            .orElseThrow(() -> new RuntimeException("Çalışan bulunamadı: " + id));
    }
    
    public void calisanSil(Long id) {
        if (!calisanRepository.existsById(id)) {
            throw new RuntimeException("Çalışan bulunamadı: " + id);
        }
        
        // Çalışanın projesi varsa silinmesini engelle
        Long projeCount = calisanRepository.countProjelerByCalisan(id);
        if (projeCount > 0) {
            throw new RuntimeException("Bu çalışanın aktif projeleri bulunmaktadır. Önce projelerden çıkarınız.");
        }
        
        calisanRepository.deleteById(id);
    }
    
    public List<Calisan> calisanAra(String arama) {
        if (arama == null || arama.trim().isEmpty()) {
            return tumCalisanlariGetir();
        }
        return calisanRepository.findByAdOrSoyadOrEmailContainingIgnoreCase(arama.trim());
    }
    
    public List<Calisan> pozisyonaGoreCalisanlar(String pozisyon) {
        return calisanRepository.findByPozisyon(pozisyon);
    }
    
    public List<Calisan> projedeOlmayanCalisanlar(Long projeId) {
        return calisanRepository.findCalisanlarNotInProje(projeId);
    }
    
    public boolean calisanVarMi(Long id) {
        return calisanRepository.existsById(id);
    }
}