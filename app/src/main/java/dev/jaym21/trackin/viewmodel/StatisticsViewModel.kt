package dev.jaym21.trackin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jaym21.trackin.model.Session
import dev.jaym21.trackin.repo.SessionRepository
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val sessionRepository: SessionRepository): ViewModel() {

    val sessionsOrderByDate: LiveData<List<Session>> = sessionRepository.getAllSessionsOrderByDate()
}