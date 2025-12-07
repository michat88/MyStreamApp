package com.example.simplestream;

public class Movie {
    // 3 hal penting yang dimiliki setiap film
    private String title;
    private String posterUrl;
    private String videoUrl;

    // Cara membuat film baru (Constructor)
    public Movie(String title, String posterUrl, String videoUrl) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.videoUrl = videoUrl;
    }

    // Fungsi untuk mengambil data (Getter)
    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
