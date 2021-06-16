package org.d3if4076.modul5.ui.history

import androidx.lifecycle.ViewModel
import org.d3if4076.modul5.db.BmiDao

class HistoryViewModel(db: BmiDao) : ViewModel() {
    val data = db.getLastBmi()
}