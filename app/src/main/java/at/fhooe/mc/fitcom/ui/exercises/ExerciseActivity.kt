package at.fhooe.mc.fitcom.ui.exercises

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.ActivityExerciseBinding
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.ExercisePoolActivity
import at.fhooe.mc.fitcom.ui.workouts.WorkoutsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseBinding
    private lateinit var mName: String
    private var mAuth = FirebaseAuth.getInstance()
    private var mDbCollection = Firebase.firestore.collection("users").document(mAuth.uid.toString())
    private var mExerciseNames = ArrayList<String>()
    private var mWeights = ArrayList<Float>()
    private var mReps = ArrayList<Int>()
    private var mSets = ArrayList<Int>()
    private lateinit var mAdapter: ExerciseAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var extras: Bundle? = intent.extras
        if(extras != null) {
            mName = extras.getString("workout").toString()
        }

        supportActionBar?.title = mName


        //TODO remove after testing
        mAdapter = ExerciseAdapter(mExerciseNames, mWeights, mReps, mSets)

        if (mAuth.currentUser != null) {


            mDbCollection.collection("exercises").document(mName).get().addOnCompleteListener() {
                if (it.isSuccessful) {
                    if(it.result!!.exists()) {

                        mExerciseNames = it.result!!["exerciseNames"] as ArrayList<String>
                        mWeights = it.result!!["weights"] as ArrayList<Float>
                        mReps = it.result!!["reps"] as ArrayList<Int>
                        mSets = it.result!!["sets"] as ArrayList<Int>

                        binding.activityExerciseTextView.isVisible = mExerciseNames.isEmpty()

                        mAdapter = ExerciseAdapter(mExerciseNames, mWeights, mReps, mSets)

                        binding.activityExerciseRecyclerView.adapter = mAdapter
                        binding.activityExerciseRecyclerView.layoutManager =
                            LinearLayoutManager(binding.root.context)
                    }


                } else {
                    Toast.makeText(binding.root.context, "Fetching Data failed!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }



        binding.activityExerciseFabAddExercise.setOnClickListener{

            startActivity(Intent(binding.root.context, ExercisePoolActivity::class.java))


            mAdapter.addItem("Bench Press", 50F, 6, 3)


        }

    }

    override fun onPause() {
        super.onPause()
        val exerciseData = hashMapOf("exerciseNames" to mExerciseNames, "weights" to mWeights, "reps" to mReps, "sets" to mSets)
        mDbCollection.collection("exercises").document(mName).set(exerciseData)
    }

}