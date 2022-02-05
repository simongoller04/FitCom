package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.ActivityExerciseBinding
import at.fhooe.mc.fitcom.databinding.ActivityExercisePoolBinding

class ExercisePoolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExercisePoolBinding
    private var mExerciseNames = ArrayList<String>()
    private var mExerciseIcons = ArrayList<Drawable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExercisePoolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Exercises"


        mExerciseNames.add("")


    }


}