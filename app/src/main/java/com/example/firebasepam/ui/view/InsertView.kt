package com.example.firebasepam.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebasepam.ui.viewmodel.FormErrorState
import com.example.firebasepam.ui.viewmodel.FormState
import com.example.firebasepam.ui.viewmodel.InsertUiState
import com.example.firebasepam.ui.viewmodel.InsertViewModel
import com.example.firebasepam.ui.viewmodel.MahasiswaEvent
import com.example.firebasepam.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMhsView (
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {

    val uiState = viewModel.uiState // State Utama Untuk Loading, Success, Error
    val uiEvent = viewModel.uiEvent // State Untuk Form Dan Validasi
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    //Observasi Perubahan State Untuk Snackbar Dan Navigasi
    LaunchedEffect(uiState) {
        when (uiState) {
            is FormState.Success -> {
                println(
                    "InsertMhsView: uiState is FormState.Success, Navigate to Home" + uiState.message
                )

                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
                delay(700)
                onNavigate()
                viewModel.resetSnackBarMessage()
            }
            is FormState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
            else -> Unit
        }
    }

    Scaffold (
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Tambah Mahasiswa") },
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text(text = "Back")
                    }
                }
            )
        }
    ){
            padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ){
            InsertBodyMhs(
                uiState = uiEvent,
                homeUiState = uiState,
                onValueChange = { updatedEvent ->
                    viewModel.UpdateState(updatedEvent)
                },
                onClick = {
                    if (viewModel.validateFields()) {
                        viewModel.insertMhs()
                    }
                }
            )
        }
    }
}

@Composable
fun InsertBodyMhs (
    modifier: Modifier = Modifier,
    onValueChange: (MahasiswaEvent) -> Unit,
    uiState: InsertUiState,
    onClick: () -> Unit,
    homeUiState: FormState
) {

    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = homeUiState !is FormState.Loading
        ) {
            if (homeUiState is FormState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(8.dp)
                )
                Text("Loading.....")
            } else {
                Text("Add")
            }
        }
        FormMahasiswa(
            mahasiswaEvent = uiState.insertUiEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = modifier.fillMaxWidth()
        )
    }
}


@Composable
fun FormMahasiswa(
    mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    onValueChange: (MahasiswaEvent) -> Unit,
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier
) {
    val jeniskelamin = listOf("Laki-laki", "Perempuan")
    val kelas = listOf("A", "B", "C", "D", "E")

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nama,
            onValueChange = { onValueChange(mahasiswaEvent.copy(nama = it)) },
            label = { Text(text = "Nama") },
            isError = errorState.nama != null,
            placeholder = { Text(text = "Masukkan Nama") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person Icon"
                )
            }
        )
        Text(
            text = errorState.nama ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nim,
            onValueChange = { onValueChange(mahasiswaEvent.copy(nim = it)) },
            label = { Text(text = "NIM") },
            isError = errorState.nim != null,
            placeholder = { Text(text = "Masukkan NIM") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Person Icon"
                )
            }
        )
        Text(
            text = errorState.nim ?: "",
            color = Color.Red
        )
        Text(text = "Jenis Kelamin")
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            jeniskelamin.forEach { jk ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = mahasiswaEvent.jeniskelamin == jk,
                        onClick = {
                            onValueChange(mahasiswaEvent.copy(jeniskelamin = jk))
                        }
                    )
                    Text(text = jk)
                }
            }
        }
        Text(
            text = errorState.jeniskelamin ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.alamat,
            onValueChange = { onValueChange(mahasiswaEvent.copy(alamat = it)) },
            label = { Text(text = "Alamat") },
            isError = errorState.alamat != null,
            placeholder = { Text(text = "Masukkan Alamat") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Person Icon"
                )
            }
        )
        Text(
            text = errorState.alamat ?: "",
            color = Color.Red
        )

        Text(text = "Kelas")
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            kelas.forEach { kelas ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = mahasiswaEvent.kelas == kelas,
                        onClick = {
                            onValueChange(mahasiswaEvent.copy(kelas = kelas))
                        }
                    )
                    Text(text = kelas)
                }
            }
        }
        Text(
            text = errorState.kelas ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.angkatan,
            onValueChange = { onValueChange(mahasiswaEvent.copy(angkatan = it)) },
            label = { Text(text = "Angkatan") },
            isError = errorState.angkatan != null,
            placeholder = { Text(text = "Masukkan Angkatan") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Person Icon"
                )
            }
        )
        Text(
            text = errorState.angkatan ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.judulskripsi,
            onValueChange = { onValueChange(mahasiswaEvent.copy(judulskripsi = it)) },
            label = { Text(text = "Judul Skripsi") },
            isError = errorState.angkatan != null,
            placeholder = { Text(text = "Masukkan Judul Skripsi") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Person Icon"
                )
            }
        )
        Text(
            text = errorState.judulskripsi ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dosen1,
            onValueChange = { onValueChange(mahasiswaEvent.copy(dosen1 = it)) },
            label = { Text(text = "Dosen Pembimbing 1") },
            isError = errorState.angkatan != null,
            placeholder = { Text(text = "Masukkan Dosen Pembimbing 1") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Person Icon"
                )
            }
        )
        Text(
            text = errorState.dosen1 ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dosen2,
            onValueChange = { onValueChange(mahasiswaEvent.copy(dosen2 = it)) },
            label = { Text(text = "Dosen Pembimbing 2") },
            isError = errorState.angkatan != null,
            placeholder = { Text(text = "Masukkan Dosen Pembimbing 2") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Person Icon"
                )
            }
        )
        Text(
            text = errorState.dosen2 ?: "",
            color = Color.Red
        )
    }
}