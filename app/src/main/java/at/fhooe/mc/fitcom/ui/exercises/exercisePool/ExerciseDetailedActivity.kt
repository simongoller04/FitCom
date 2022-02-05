package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.ActivityExerciseDetailedBinding

class ExerciseDetailedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // plus button pressed, increase sets by 1
        binding.activityExerciseDetailedSetsPlusButton.setOnClickListener {
            var amountSets = binding.activityExerciseDetailedSetsAmountTextview.text.toString().toInt() + 1
            binding.activityExerciseDetailedSetsAmountTextview.text = amountSets.toString()
        }

        // minus button pressed, decrease sets by 1
        binding.activityExerciseDetailedSetsMinusButton.setOnClickListener {
            var amountSets = binding.activityExerciseDetailedSetsAmountTextview.text.toString().toInt() - 1
            binding.activityExerciseDetailedSetsAmountTextview.text = amountSets.toString()
        }

        // plus button pressed, increase reps by 1
        binding.activityExerciseDetailedRepsPlusButton.setOnClickListener {
            var amountSets = binding.activityExerciseDetailedRepsAmountTextview.text.toString().toInt() + 1
            binding.activityExerciseDetailedRepsAmountTextview.text = amountSets.toString()
        }

        // minus button pressed, decrease reps by 1
        binding.activityExerciseDetailedRepsMinusButton.setOnClickListener {
            var amountSets = binding.activityExerciseDetailedRepsAmountTextview.text.toString().toInt() - 1
            binding.activityExerciseDetailedRepsAmountTextview.text = amountSets.toString()
        }

        // plus button pressed, increase weight by 0.5
        binding.activityExerciseDetailedWeightPlusButton.setOnClickListener {
            var amountSets = binding.activityExerciseDetailedWeightAmountTextview.text.toString().toFloat() + 0.5f
            binding.activityExerciseDetailedWeightAmountTextview.text = amountSets.toString()
        }

        // minus button pressed, decrease weight by 0.5
        binding.activityExerciseDetailedWeightMinusButton.setOnClickListener {
            var amountSets = binding.activityExerciseDetailedWeightAmountTextview.text.toString().toFloat() - 0.5f
            binding.activityExerciseDetailedWeightAmountTextview.text = amountSets.toString()
        }

        binding.activityExerciseDetailedCancleButton.setOnClickListener {
            finish()
        }

        binding.activityExerciseDetailedDoneButton.setOnClickListener {
            //TODO
        }
    }
}