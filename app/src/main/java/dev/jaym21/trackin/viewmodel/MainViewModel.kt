package dev.jaym21.trackin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaym21.trackin.model.Run
import dev.jaym21.trackin.repo.RunRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val runRepository: RunRepository): ViewModel() {

    private val _runsOrderByDate: MutableLiveData<List<Run>> = MutableLiveData()
    val runsOrderByDate: LiveData<List<Run>> = _runsOrderByDate

    fun addRun(run: Run) = viewModelScope.launch(Dispatchers.IO) {
        runRepository.insertRun(run)
    }

    fun deleteRun(run: Run) = viewModelScope.launch(Dispatchers.IO) {
        runRepository.deleteRun(run)
    }

    fun getAllRunsOrderByDate() {
        _runsOrderByDate.postValue(runRepository.getAllRunsOrderByDate().value)
    }
}