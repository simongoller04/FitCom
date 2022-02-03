package at.fhooe.mc.fitcom.ui.nfc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NFCViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the NFC Fragment"
    }
    val text: LiveData<String> = _text
}