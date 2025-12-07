package com.example.simplestream;

import java.io.Serializable;

public class Movie implements Serializable {
    // Data penting untuk satu film
    private String title;       // Judul Film
    private String posterUrl;   // Link Gambar Poster
    private String detailUrl;   // Link Halaman Web Film (misal: adicinemax.com/film-a)
    private String quality;     // Kualitas (HD/CAM) - Optional
    private String rating;      // Rating (8.5) - Optional

    // Constructor kosong (Penting untuk beberapa fungsi sistem)
    public Movie() {
    }

    // Constructor Utama
    public Movie(String title, String posterUrl, String detailUrl) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.detailUrl = detailUrl;
    }

    // Constructor Lengkap (Untuk tampilan lebih detail)
    public Movie(String title, String posterUrl, String detailUrl, String quality, String rating) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.detailUrl = detailUrl;
        this.quality = quality;
        this.rating = rating;
    }

    // --- GETTER & SETTER ---
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getQuality() {
        return quality == null ? "" : quality; // Biar tidak error kalau kosong
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getRating() {
        return rating == null ? "N/A" : rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
