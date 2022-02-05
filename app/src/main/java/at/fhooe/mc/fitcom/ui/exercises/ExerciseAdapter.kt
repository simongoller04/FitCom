package at.fhooe.mc.fitcom.ui.exercises

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.ExercisePoolActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExerciseAdapter (val exercises: ArrayList<String>, val weights: ArrayList<Float>, val reps: ArrayList<Int>, val sets: ArrayList<Int>): RecyclerView.Adapter<ExerciseViewHolder>(){

    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = Firebase.firestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.exercise_item, null)
            return ExerciseViewHolder(root)
        }
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.mExerciseName.text = exercises[position]
        holder.mWeight.text = weights[position].toString() + "kg"
        holder.mReps.text = reps[position].toString() + " reps"
        holder.mSets.text = sets[position].toString() + " sets"

        holder.mCardView.setOnClickListener {
            holder.mCardView.isChecked = true   //TODO ("Change to open new activity")
        }



    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun addItem(_exerciseNameAdd: String, _weightAdd: Float, _repsAdd: Int, _setsAdd: Int) {
        exercises.add(_exerciseNameAdd)
        weights.add(_weightAdd)
        reps.add(_repsAdd)
        sets.add(_setsAdd)

        notifyItemInserted(exercises.size-1)
    }
}