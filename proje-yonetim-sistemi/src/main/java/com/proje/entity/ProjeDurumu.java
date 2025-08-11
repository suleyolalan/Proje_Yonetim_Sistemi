package com.proje.entity;

public enum ProjeDurumu {
    YENI("Yeni"),
    DEVAM_EDIYOR("Devam Ediyor"),
    TAMAMLANDI("Tamamlandı"),
    IPTAL_EDILDI("İptal Edildi");
    
    private final String displayName;
    
    ProjeDurumu(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}