package at.fhooe.mc.fitcom.ui.workouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkoutsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Exercises Fragment"
    }
    val text: LiveData<String> = _text
}