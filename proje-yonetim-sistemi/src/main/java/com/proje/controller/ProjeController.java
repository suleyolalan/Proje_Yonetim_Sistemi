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

import com.proje.entity.Proje;
import com.proje.entity.ProjeDurumu;
import com.proje.service.ProjeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projeler")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjeController {
    
    @Autowired
    private ProjeService projeService;
    
    @GetMapping
    public ResponseEntity<List<Proje>> tumProjeler() {
        try {
            List<Proje> projeler = projeService.tumProjeleriGetir();
            return ResponseEntity.ok(projeler);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Proje> projeGetir(@PathVariable Long id) {
        try {
            return projeService.projeGetir(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> projeOlustur(@Valid @RequestBody Proje proje) {
        try {
            Proje yeniProje = projeService.projeKaydet(proje);
            return ResponseEntity.ok(yeniProje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> projeGuncelle(@PathVariable Long id, @Valid @RequestBody Proje proje) {
        try {
            Proje guncelProje = projeService.projeGuncelle(id, proje);
            return ResponseEntity.ok(guncelProje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> projeSil(@PathVariable Long id) {
        try {
            projeService.projeSil(id);
            return ResponseEntity.ok().body("Proje başarıyla silindi");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/{projeId}/calisanlar/{calisanId}")
    public ResponseEntity<?> calisanAta(@PathVariable Long projeId, @PathVariable Long calisanId) {
        try {
            Proje proje = projeService.calisanAta(projeId, calisanId);
            return ResponseEntity.ok(proje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{projeId}/calisanlar/{calisanId}")
    public ResponseEntity<?> calisanCikar(@PathVariable Long projeId, @PathVariable Long calisanId) {
        try {
            Proje proje = projeService.calisanCikar(projeId, calisanId);
            return ResponseEntity.ok(proje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/durum/{durum}")
    public ResponseEntity<List<Proje>> durumGoreProjeler(@PathVariable ProjeDurumu durum) {
        try {
            List<Proje> projeler = projeService.durumGoreProje(durum);
            return ResponseEntity.ok(projeler);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/ara")
    public ResponseEntity<List<Proje>> projeAra(@RequestParam(required = false) String q) {
        try {
            List<Proje> sonuclar = projeService.projeAra(q);
            return ResponseEntity.ok(sonuclar);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/calisan/{calisanId}")
    public ResponseEntity<List<Proje>> calisanProjeler(@PathVariable Long calisanId) {
        try {
            List<Proje> projeler = projeService.calisanaGoreProjeler(calisanId);
            return ResponseEntity.ok(projeler);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/geciken")
    public ResponseEntity<List<Proje>> gecikmisProjeler() {
        try {
            List<Proje> projeler = projeService.gecikmisProjeler();
            return ResponseEntity.ok(projeler);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}/durum")
    public ResponseEntity<?> projeDurumDegistir(@PathVariable Long id, @RequestBody ProjeDurumu yeniDurum) {
        try {
            Proje proje = projeService.projeDurumDegistir(id, yeniDurum);
            return ResponseEntity.ok(proje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}