package com.proje.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proje.entity.Calisan;
import com.proje.service.CalisanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/calisanlar")
@CrossOrigin(origins = "http://localhost:3000")
public class CalisanController {
    
    @Autowired
    private CalisanService calisanService;
    
    @GetMapping
    public ResponseEntity<List<Calisan>> tumCalisanlar() {
        try {
            List<Calisan> calisanlar = calisanService.tumCalisanlariGetir();
            return ResponseEntity.ok(calisanlar);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Calisan> calisanGetir(@PathVariable Long id) {
        try {
            return calisanService.calisanGetir(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Calisan> emailIleCalisanGetir(@PathVariable String email) {
        try {
            return calisanService.emailIleCalisanGetir(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> calisanOlustur(@Valid @RequestBody Calisan calisan) {
        try {
            Calisan yeniCalisan = calisanService.calisanKaydet(calisan);
            return ResponseEntity.ok(yeniCalisan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> calisanGuncelle(@PathVariable Long id, @Valid @RequestBody Calisan calisan) {
        try {
            Calisan guncelCalisan = calisanService.calisanGuncelle(id, calisan);
            return ResponseEntity.ok(guncelCalisan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> calisanSil(@PathVariable Long id) {
        try {
            calisanService.calisanSil(id);
            return ResponseEntity.ok().body("Çalışan başarıyla silindi");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/ara")
    public ResponseEntity<List<Calisan>> calisanAra(@RequestParam(required = false) String q) {
        try {
            List<Calisan> sonuclar = calisanService.calisanAra(q);
            return ResponseEntity.ok(sonuclar);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/pozisyon/{pozisyon}")
    public ResponseEntity<List<Calisan>> pozisyonaGoreCalisanlar(@PathVariable String pozisyon) {
        try {
            List<Calisan> calisanlar = calisanService.pozisyonaGoreCalisanlar(pozisyon);
            return ResponseEntity.ok(calisanlar);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}