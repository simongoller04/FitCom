package at.fhooe.mc.fitcom.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExercisesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Exercises Fragment"
    }
    val text: LiveData<String> = _text
}