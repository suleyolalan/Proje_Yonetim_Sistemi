package com.proje.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proje.entity.Calisan;
import com.proje.entity.Proje;
import com.proje.entity.ProjeDurumu;
import com.proje.repository.CalisanRepository;
import com.proje.repository.ProjeRepository;

@Service
@Transactional
public class ProjeService {
    
    @Autowired
    private ProjeRepository projeRepository;
    
    @Autowired
    private CalisanRepository calisanRepository;
    
    public List<Proje> tumProjeleriGetir() {
        return projeRepository.findAll();
    }
    
    public Optional<Proje> projeGetir(Long id) {
        return projeRepository.findById(id);
    }
    
    public Proje projeKaydet(Proje proje) {
        // Tarihleri kontrol et
        if (proje.getBaslangicTarihi() != null && proje.getBitisTarihi() != null) {
            if (proje.getBaslangicTarihi().isAfter(proje.getBitisTarihi())) {
                throw new RuntimeException("Başlangıç tarihi bitiş tarihinden sonra olamaz");
            }
        }
        
        return projeRepository.save(proje);
    }
    
    public Proje projeGuncelle(Long id, Proje guncelProje) {
        return projeRepository.findById(id)
            .map(mevcutProje -> {
                // Tarihleri kontrol et
                if (guncelProje.getBaslangicTarihi() != null && guncelProje.getBitisTarihi() != null) {
                    if (guncelProje.getBaslangicTarihi().isAfter(guncelProje.getBitisTarihi())) {
                        throw new RuntimeException("Başlangıç tarihi bitiş tarihinden sonra olamaz");
                    }
                }
                
                mevcutProje.setAd(guncelProje.getAd());
                mevcutProje.setAciklama(guncelProje.getAciklama());
                mevcutProje.setBaslangicTarihi(guncelProje.getBaslangicTarihi());
                mevcutProje.setBitisTarihi(guncelProje.getBitisTarihi());
                mevcutProje.setDurum(guncelProje.getDurum());
                
                return projeRepository.save(mevcutProje);
            })
            .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + id));
    }
    
    public void projeSil(Long id) {
        if (!projeRepository.existsById(id)) {
            throw new RuntimeException("Proje bulunamadı: " + id);
        }
        projeRepository.deleteById(id);
    }
    
    public Proje calisanAta(Long projeId, Long calisanId) {
        Proje proje = projeRepository.findById(projeId)
            .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + projeId));
        
        Calisan calisan = calisanRepository.findById(calisanId)
            .orElseThrow(() -> new RuntimeException("Çalışan bulunamadı: " + calisanId));
        
        proje.addCalisan(calisan);
        return projeRepository.save(proje);
    }
    
    public Proje calisanCikar(Long projeId, Long calisanId) {
        Proje proje = projeRepository.findById(projeId)
            .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + projeId));
        
        Calisan calisan = calisanRepository.findById(calisanId)
            .orElseThrow(() -> new RuntimeException("Çalışan bulunamadı: " + calisanId));
        
        proje.removeCalisan(calisan);
        return projeRepository.save(proje);
    }
    
    public List<Proje> durumGoreProje(ProjeDurumu durum) {
        return projeRepository.findByDurum(durum);
    }
    
    public List<Proje> projeAra(String arama) {
        if (arama == null || arama.trim().isEmpty()) {
            return tumProjeleriGetir();
        }
        return projeRepository.findByAdOrAciklamaContainingIgnoreCase(arama.trim());
    }
    
    public List<Proje> calisanaGoreProjeler(Long calisanId) {
        return projeRepository.findProjelerByCalisan(calisanId);
    }
    
    public List<Proje> gecikmisProjeler() {
        return projeRepository.findGecikmisProjeler(LocalDate.now());
    }
    
    public List<Proje> tarihAraligiProje(LocalDate baslangic, LocalDate bitis) {
        return projeRepository.findByBaslangicTarihiBetween(baslangic, bitis);
    }
    
    public Proje projeDurumDegistir(Long projeId, ProjeDurumu yeniDurum) {
        return projeRepository.findById(projeId)
            .map(proje -> {
                proje.setDurum(yeniDurum);
                return projeRepository.save(proje);
            })
            .orElseThrow(() -> new RuntimeException("Proje bulunamadı: " + projeId));
    }
    
    public boolean projeVarMi(Long id) {
        return projeRepository.existsById(id);
    }
}