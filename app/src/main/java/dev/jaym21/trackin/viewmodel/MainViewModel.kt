package dev.jaym21.trackin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaym21.trackin.model.Session
import dev.jaym21.trackin.repo.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val sessionRepository: SessionRepository): ViewModel() {

    val totalDistance: LiveData<Int> = sessionRepository.getTotalDistance()
    val totalCaloriesBurned: LiveData<Int> = sessionRepository.getTotalCaloriesBurned()
    val totalSessionTime: LiveData<Long> = sessionRepository.getTotalSessionTime()
    val totalAverageSpeed: LiveData<Float> = sessionRepository.getTotalAverageSpeed()

    val sessionsOrderByDate: LiveData<List<Session>> = sessionRepository.getAllSessionsOrderByDate()

    fun addRun(session: Session) = viewModelScope.launch(Dispatchers.IO) {
        sessionRepository.insertSession(session)
    }

    fun deleteRun(session: Session) = viewModelScope.launch(Dispatchers.IO) {
        sessionRepository.deleteSession(session)
    }
}