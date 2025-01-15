package com.example.firebasepam.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasepam.Model.Mahasiswa
import com.example.firebasepam.repository.MahasiswaRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val mhsRepository: MahasiswaRepository) : ViewModel() {

    var uiState by mutableStateOf(DetailUiState())
        private set

    fun fetchDetailMahasiswa(nim: String) {
        viewModelScope.launch {
            uiState = DetailUiState(isLoding = true)
            try {
                val mahasiswa = mhsRepository.getMahasiswaBynim(nim)
//                uiState = DetailUiState(detailUiEvent = mahasiswa.toMhsModel())
            } catch (e: Exception) {
                e.printStackTrace()
                uiState = DetailUiState(
                    isError = true,
                    errorMessages = "Failed to fetch details: ${e.message}"
                )
            }
        }
    }
}

data class DetailUiState(
    val detailUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isLoding: Boolean = false,
    val isError: Boolean = false,
    val errorMessages: String = ""
) {
    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != MahasiswaEvent()
}

fun Mahasiswa.toMhsModel(): MahasiswaEvent {
    return MahasiswaEvent(
        nim = nim,
        nama = nama,
        alamat = alamat,
        jeniskelamin = jeniskelamin,
        kelas = kelas,
        angkatan = angkatan,
        judulskripsi = judulskripsi,
        dosen1 = dosen1,
        dosen2 = dosen2
    )
}