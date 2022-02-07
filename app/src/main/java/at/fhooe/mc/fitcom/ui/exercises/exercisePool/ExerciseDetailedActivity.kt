package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.ActivityExerciseDetailedBinding
import at.fhooe.mc.fitcom.ui.exercises.ExerciseActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class ExerciseDetailedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailedBinding
    private lateinit var mDataExercise: SingleCompleteExerciseData
    private var mAuth = FirebaseAuth.getInstance()
    private var mDbCollection =
        Firebase.firestore.collection("users").document(mAuth.uid.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_)

        binding = ActivityExerciseDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var extras: Bundle? = intent.extras
        if (extras != null) {
            mDataExercise = extras.getSerializable("exerciseData") as SingleCompleteExerciseData
            supportActionBar!!.title = mDataExercise.name
            binding.activityExerciseDetailedRepsAmountTextview.text = mDataExercise.reps.toString()
            binding.activityExerciseDetailedSetsAmountTextview.text = mDataExercise.sets.toString()
            binding.activityExerciseDetailedWeightAmountTextview.text =
                mDataExercise.weight.toString()
            Glide.with(this)
                .load(mDataExercise.image)
                .into(binding.activityExerciseDetailedImageView)
        }

        // plus button pressed, increase sets by 1
        binding.activityExerciseDetailedSetsPlusButton.setOnClickListener {
            var amountSets =
                binding.activityExerciseDetailedSetsAmountTextview.text.toString().toInt() + 1
            binding.activityExerciseDetailedSetsAmountTextview.text = amountSets.toString()
        }

        // minus button pressed, decrease sets by 1
        binding.activityExerciseDetailedSetsMinusButton.setOnClickListener {
            var amountSets =
                binding.activityExerciseDetailedSetsAmountTextview.text.toString().toInt() - 1
            binding.activityExerciseDetailedSetsAmountTextview.text = amountSets.toString()
        }

        // plus button pressed, increase reps by 1
        binding.activityExerciseDetailedRepsPlusButton.setOnClickListener {
            var amountSets =
                binding.activityExerciseDetailedRepsAmountTextview.text.toString().toInt() + 1
            binding.activityExerciseDetailedRepsAmountTextview.text = amountSets.toString()
        }

        // minus button pressed, decrease reps by 1
        binding.activityExerciseDetailedRepsMinusButton.setOnClickListener {
            var amountSets =
                binding.activityExerciseDetailedRepsAmountTextview.text.toString().toInt() - 1
            binding.activityExerciseDetailedRepsAmountTextview.text = amountSets.toString()
        }

        // plus button pressed, increase weight by 0.5
        binding.activityExerciseDetailedWeightPlusButton.setOnClickListener {
            var amountSets = binding.activityExerciseDetailedWeightAmountTextview.text.toString()
                .toFloat() + 0.5f
            binding.activityExerciseDetailedWeightAmountTextview.text = amountSets.toString()
        }

        // minus button pressed, decrease weight by 0.5
        binding.activityExerciseDetailedWeightMinusButton.setOnClickListener {
            var amountSets = binding.activityExerciseDetailedWeightAmountTextview.text.toString()
                .toFloat() - 0.5f
            binding.activityExerciseDetailedWeightAmountTextview.text = amountSets.toString()
        }

        binding.activityExerciseDetailedCancleButton.setOnClickListener {
            finish()
        }

        binding.activityExerciseDetailedDoneButton.setOnClickListener {
            val data = SingleCompleteExerciseData(
                mDataExercise.name,
                binding.activityExerciseDetailedSetsAmountTextview.text.toString().toInt(),
                binding.activityExerciseDetailedRepsAmountTextview.text.toString().toInt(),
                binding.activityExerciseDetailedWeightAmountTextview.text.toString().toFloat(),
                mDataExercise.image
            )
            val json = Gson().toJson(data)
            val sharedPref =
                getSharedPreferences("at.fhooe.mc.fitcom.exerciseActivity", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("exerciseData", json.toString())
                apply()
            }

            val amountOfReps = binding.activityExerciseDetailedSetsAmountTextview.text.toString()
                .toInt() *
                    binding.activityExerciseDetailedRepsAmountTextview.text.toString()
                        .toInt()

            val totalWeightThisExercise: Double =
                binding.activityExerciseDetailedWeightAmountTextview.text.toString()
                    .toDouble() * amountOfReps

            mDbCollection.update(
                "totalWeightLifted",
                FieldValue.increment(
                    totalWeightThisExercise
                )
            )

            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}