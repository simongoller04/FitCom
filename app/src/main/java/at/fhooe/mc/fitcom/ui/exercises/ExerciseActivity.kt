package at.fhooe.mc.fitcom.ui.exercises

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.ActivityExerciseBinding
import at.fhooe.mc.fitcom.databinding.ActivityMainBinding

class ExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseBinding
    private lateinit var mName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var extras: Bundle? = intent.extras
        if(extras != null) {
            mName = extras.getString("workout").toString()
        }

        supportActionBar?.title = mName
    }
}