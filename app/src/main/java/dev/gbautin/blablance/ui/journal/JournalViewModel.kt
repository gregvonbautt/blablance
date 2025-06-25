package dev.gbautin.blablance.ui.journal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.gbautin.blablance.data.EventManager
import dev.gbautin.blablance.data.ScoreEvent

class JournalViewModel : ViewModel() {

    val events: LiveData<List<ScoreEvent>> = EventManager.events
}