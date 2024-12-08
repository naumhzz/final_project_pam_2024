package com.example.p6firebase.Main;

public class Air {
    private String id;
    private String waktu;
    private String jumlahAir;
    private String tanggal;

    public boolean isSudahSum() {
        return sudahSum;
    }

    public void setSudahSum(boolean sudahSum) {
        this.sudahSum = sudahSum;
    }

    private boolean sudahSum;

    public Air() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getJumlahAir() {
        return jumlahAir;
    }

    public void setJumlahAir(String jumlahAir) {
        this.jumlahAir = jumlahAir;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
