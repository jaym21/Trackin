package dev.jaym21.trackin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _runsOrderByDate: MutableLiveData<List<Session>> = MutableLiveData()
    val runsOrderByDate: LiveData<List<Session>> = _runsOrderByDate

    fun addRun(session: Session) = viewModelScope.launch(Dispatchers.IO) {
        sessionRepository.insertSession(session)
    }

    fun deleteRun(session: Session) = viewModelScope.launch(Dispatchers.IO) {
        sessionRepository.deleteSession(session)
    }

    fun getAllRunsOrderByDate() {
        _runsOrderByDate.postValue(sessionRepository.getAllSessionsOrderByDate().value)
    }
}