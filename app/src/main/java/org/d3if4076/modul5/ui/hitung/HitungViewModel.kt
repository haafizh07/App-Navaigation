package org.d3if4076.modul5.ui.hitung

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if4076.modul5.data.HasilBmi
import org.d3if4076.modul5.data.KategoriBmi
import org.d3if4076.modul5.db.BmiDao
import org.d3if4076.modul5.db.BmiEntity

class HitungViewModel(private val db: BmiDao) : ViewModel() {
    private val navigasi = MutableLiveData<KategoriBmi?>()
    private val hasilBmi = MutableLiveData<HasilBmi?>()
    fun hitungBmi(berat: String, tinggi: String, isMale: Boolean) {
        val tinggiCm = tinggi.toFloat() / 100
        val bmi = berat.toFloat() / (tinggiCm * tinggiCm)
        val kategori = if (isMale) {
            when {
                bmi < 20.5 -> KategoriBmi.KURUS
                bmi >= 27.0 -> KategoriBmi.GEMUK
                else -> KategoriBmi.IDEAL
            }
        }
        else {
            when {
                bmi < 18.5 -> KategoriBmi.KURUS
                bmi >= 25.0 -> KategoriBmi.GEMUK
                else -> KategoriBmi.IDEAL
            }
        }
        hasilBmi.value = HasilBmi(bmi, kategori)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val dataBmi = BmiEntity(
                    berat = berat.toFloat(),
                    tinggi = tinggi.toFloat(),
                    isMale = isMale
                )
                db.insert(dataBmi)
            }
        }

    }
    fun mulaiNavigasi() {
        navigasi.value = hasilBmi.value?.kategori
    }
    fun selesaiNavigasi() {
        navigasi.value = null
    }
    fun getNavigasi() : LiveData<KategoriBmi?> = navigasi
    fun getHasilBmi() : LiveData<HasilBmi?> = hasilBmi
}



