const express = require('express');
const { Pool } = require('pg');
const cors = require('cors');

const app = express();
const PORT = 3000;

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.static('public'));

app.get('/', (req, res) => {
  res.sendFile(__dirname + '/public/index.html');
});

// PostgreSQL bağlantısı
const pool = new Pool({
    user: 'postgres',
    host: 'localhost',
    database: 'proje_yonetim_db',
    password: '336',
    port: 5432,
});

// Veritabanı bağlantı testi
pool.connect((err, client, release) => {
    if (err) {
        console.error('Veritabanı bağlantı hatası:', err);
    } else {
        console.log('Veritabanı bağlantısı başarılı!');
        release();
    }
});

// Ana route
app.get('/', (req, res) => {
    res.json({ 
        message: 'Proje Yönetim API çalışıyor!', 
        endpoints: [
            'GET /api/employees - Tüm çalışanlar',
            'POST /api/employees - Yeni çalışan',
            'DELETE /api/employees/:id - Çalışan sil',
            'GET /api/projects - Tüm projeler',
            'POST /api/projects - Yeni proje',
            'DELETE /api/projects/:id - Proje sil'
        ]
    });
});

// ÇALIŞANLAR ENDPOINT'LERİ

// Tüm çalışanları getir
app.get('/api/employees', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM calisanlar ORDER BY id');
        res.json(result.rows);
    } catch (err) {
        console.error('Çalışanları getirme hatası:', err);
        res.status(500).json({ error: 'Çalışanlar getirilemedi' });
    }
});

// Yeni çalışan ekle
app.post('/api/employees', async (req, res) => {
    try {
        const { ad, soyad, email, pozisyon, telefon, baslangic_tarihi, maas } = req.body;
        
        const result = await pool.query(
            'INSERT INTO calisanlar (ad, soyad, email, pozisyon, telefon, baslangic_tarihi, maas) VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING *',
            [ad, soyad, email, pozisyon, telefon, baslangic_tarihi, maas]
        );
        
        res.status(201).json(result.rows[0]);
    } catch (err) {
        console.error('Çalışan ekleme hatası:', err);
        res.status(500).json({ error: 'Çalışan eklenemedi' });
    }
});

// Çalışan sil
app.delete('/api/employees/:id', async (req, res) => {
    try {
        const { id } = req.params;
        
        const result = await pool.query('DELETE FROM calisanlar WHERE id = $1 RETURNING *', [id]);
        
        if (result.rows.length === 0) {
            return res.status(404).json({ error: 'Çalışan bulunamadı' });
        }
        
        res.json({ message: 'Çalışan silindi', employee: result.rows[0] });
    } catch (err) {
        console.error('Çalışan silme hatası:', err);
        res.status(500).json({ error: 'Çalışan silinemedi' });
    }
});

// PROJELER ENDPOINT'LERİ

// Tüm projeleri getir
app.get('/api/projects', async (req, res) => {
    try {
        const result = await pool.query(`
            SELECT p.*, c.ad || ' ' || c.soyad as yonetici_ad 
            FROM projeler p 
            LEFT JOIN calisanlar c ON p.proje_yoneticisi_id = c.id 
            ORDER BY p.id
        `);
        res.json(result.rows);
    } catch (err) {
        console.error('Projeleri getirme hatası:', err);
        res.status(500).json({ error: 'Projeler getirilemedi' });
    }
});

// Yeni proje ekle
app.post('/api/projects', async (req, res) => {
    try {
        const { proje_adi, aciklama, baslangic_tarihi, bitis_tarihi, durum, butce, proje_yoneticisi_id } = req.body;
        
        const result = await pool.query(
            'INSERT INTO projeler (proje_adi, aciklama, baslangic_tarihi, bitis_tarihi, durum, butce, proje_yoneticisi_id) VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING *',
            [proje_adi, aciklama, baslangic_tarihi, bitis_tarihi, durum, butce, proje_yoneticisi_id]
        );
        
        res.status(201).json(result.rows[0]);
    } catch (err) {
        console.error('Proje ekleme hatası:', err);
        res.status(500).json({ error: 'Proje eklenemedi' });
    }
});

// Proje sil
app.delete('/api/projects/:id', async (req, res) => {
    try {
        const { id } = req.params;
        
        const result = await pool.query('DELETE FROM projeler WHERE id = $1 RETURNING *', [id]);
        
        if (result.rows.length === 0) {
            return res.status(404).json({ error: 'Proje bulunamadı' });
        }
        
        res.json({ message: 'Proje silindi', project: result.rows[0] });
    } catch (err) {
        console.error('Proje silme hatası:', err);
        res.status(500).json({ error: 'Proje silinemedi' });
    }
});

// PROJE-ÇALIŞAN İLİŞKİLERİ

// Projeye çalışan ata
app.post('/api/project-assignments', async (req, res) => {
    try {
        const { proje_id, calisan_id, rol } = req.body;
        
        const result = await pool.query(
            'INSERT INTO proje_calisan (proje_id, calisan_id, rol) VALUES ($1, $2, $3) RETURNING *',
            [proje_id, calisan_id, rol]
        );
        
        res.status(201).json(result.rows[0]);
    } catch (err) {
        console.error('Proje ataması hatası:', err);
        res.status(500).json({ error: 'Proje ataması yapılamadı' });
    }
});

// Proje çalışanlarını getir
app.get('/api/projects/:id/employees', async (req, res) => {
    try {
        const { id } = req.params;
        
        const result = await pool.query(`
            SELECT c.*, pc.rol, pc.katilim_tarihi 
            FROM calisanlar c 
            JOIN proje_calisan pc ON c.id = pc.calisan_id 
            WHERE pc.proje_id = $1 AND pc.aktif = true
        `, [id]);
        
        res.json(result.rows);
    } catch (err) {
        console.error('Proje çalışanları getirme hatası:', err);
        res.status(500).json({ error: 'Proje çalışanları getirilemedi' });
    }
});

// Server başlat
app.listen(PORT, () => {
    console.log(`Server ${PORT} portunda çalışıyor!`);
    console.log(`Ana sayfa: http://localhost:${PORT}`);
    console.log(`API: http://localhost:${PORT}/api/employees`);
});