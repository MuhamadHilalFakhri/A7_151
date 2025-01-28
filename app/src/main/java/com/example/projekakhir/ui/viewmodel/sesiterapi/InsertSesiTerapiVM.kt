package com.example.projekakhir.ui.viewmodel.sesiterapi

import android.app.AlertDialog
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhir.model.JenisTerapi
import com.example.projekakhir.model.Pasien
import com.example.projekakhir.model.SesiTerapi
import com.example.projekakhir.model.Terapis
import com.example.projekakhir.repository.JenisTerapiRepository
import com.example.projekakhir.repository.PasienRepository
import com.example.projekakhir.repository.SesiTerapiRepository
import com.example.projekakhir.repository.TerapisRepository
import kotlinx.coroutines.launch

class InsertSesiTerapiViewModel(
    private val sesiTerapiRepository: SesiTerapiRepository,
    private val jenisTerapiRepository: JenisTerapiRepository,
    private val terapisRepository: TerapisRepository,
    private val pasienRepository: PasienRepository
) : ViewModel() {

    var uiState by mutableStateOf(InsertSesiTerapiUiState())
        private set

    var listPasien by mutableStateOf<List<Pasien>>(listOf())
        private set
    var listTerapis by mutableStateOf<List<Terapis>>(listOf())
        private set
    var listJnsTerapi by mutableStateOf<List<JenisTerapi>>(listOf())
        private set

    init {
        loadExistingData()
    }

     fun loadExistingData() {
        viewModelScope.launch {
            try {
                // Mengambil data dari repository
                val jenisTerapiResponse = jenisTerapiRepository.getJenisTerapi()
                listJnsTerapi = jenisTerapiResponse.data
                val terapisResponse = terapisRepository.getTerapis()
                listTerapis = terapisResponse.data
                val pasienResponse = pasienRepository.getPasien()
                listPasien = pasienResponse.data
            } catch (e: Exception) {
                e.printStackTrace()
                // Tangani error (misalnya, log atau tunjukkan pesan kesalahan di UI)
            }
        }
    }

    fun updateInsertSesiTerapiState(insertUiEvent: InsertSesiTerapiUiEvent) {
        uiState = uiState.copy(insertUiEvent = insertUiEvent)
    }

    fun insertSesiTerapi() {
        viewModelScope.launch {
            try {
                sesiTerapiRepository.insertSesiTerapi(uiState.insertUiEvent.toSesiTerapi())
            } catch (e: Exception) {
                e.printStackTrace()
                // Tangani error saat penyimpanan data
            }
        }
    }
}

// UI State
data class InsertSesiTerapiUiState(
    val insertUiEvent: InsertSesiTerapiUiEvent = InsertSesiTerapiUiEvent(),
    val jenisTerapiList: List<JenisTerapi> = emptyList(),
    val terapisList: List<Terapis> = emptyList(),
    val pasienList: List<Pasien> = emptyList()
)

// UI Event Data
data class InsertSesiTerapiUiEvent(
    val idSesi: Int = 0,
    val idPasien: Int = 0,
    val idTerapis: Int = 0,
    val idJenisTerapi: Int = 0,
    val tanggalSesi: String = "",
    val catatanSesi: String = ""
)

// Extension Function
fun InsertSesiTerapiUiEvent.toSesiTerapi(): SesiTerapi = SesiTerapi(
    id_sesi = idSesi,
    id_pasien = idPasien,
    id_terapis = idTerapis,
    id_jenis_terapi = idJenisTerapi,
    tanggal_sesi = tanggalSesi,
    catatan_sesi = catatanSesi
)
fun validateFields(insertUiEvent: InsertSesiTerapiUiEvent): Boolean {
    return insertUiEvent.idPasien != 0 &&
            insertUiEvent.idTerapis != 0 &&
            insertUiEvent.idJenisTerapi != 0 &&
            insertUiEvent.tanggalSesi.isNotEmpty()
}

fun showConfirmationDialog(context: Context, onConfirm: () -> Unit) {
    AlertDialog.Builder(context)
        .setTitle("Konfirmasi")
        .setMessage("Apakah data sudah benar?")
        .setPositiveButton("Ya") { _, _ -> onConfirm() }
        .setNegativeButton("Tidak", null)
        .show()
}

fun showErrorDialog(context: Context) {
    AlertDialog.Builder(context)
        .setTitle("Peringatan")
        .setMessage("Semua kolom harus diisi!")
        .setPositiveButton("OK", null)
        .show()
}
