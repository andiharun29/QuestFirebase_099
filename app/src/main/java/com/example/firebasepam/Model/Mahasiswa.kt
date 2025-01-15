package com.example.firebasepam.Model

data class Mahasiswa (
    val nim: String ,
    val nama: String,
    val jeniskelamin: String,
    val alamat: String,
    val kelas: String,
    val angkatan: String,
    val judulskripsi: String,
    val dosen1: String,
    val dosen2: String
){
    constructor() : this("","","","","","","","","")
}
