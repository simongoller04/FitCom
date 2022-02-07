package at.fhooe.mc.fitcom.ui.exercises

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.ExerciseDetailedActivity
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.ExercisePoolActivity
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.ExercisePoolAdapter
import at.fhooe.mc.fitcom.ui.exercises.exercisePool.SingleCompleteExerciseData
import at.fhooe.mc.fitcom.ui.workouts.WorkoutsViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExerciseAdapter(
    val exercises: ArrayList<String>,
    val weights: ArrayList<Float>,
    val reps: ArrayList<Int>,
    val sets: ArrayList<Int>,
    val images: ArrayList<String>,
    private val callBack: workoutCompleted?
) : RecyclerView.Adapter<ExerciseViewHolder>() {

    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = Firebase.firestore
    private var mCheckedExercises = ArrayList<String>()
    private var alreadyCountedFlag = false

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
        holder.mCardView.isChecked = mCheckedExercises.contains(exercises[position])

        if (mCheckedExercises.size == exercises.size) {
            if (!alreadyCountedFlag) {
                alreadyCountedFlag = true
                callBack?.passResultCallback(true)
            }
        }

        val exerciseData = SingleCompleteExerciseData(
            exercises[position],
            sets[position],
            reps[position],
            weights[position],
            images[position]
        )

        if (!mCheckedExercises.contains(exercises[position])) {
            holder.mCardView.setOnClickListener {
                holder.mRoot.context.startActivity(
                    Intent(
                        holder.mRoot.context,
                        ExerciseDetailedActivity::class.java
                    ).putExtra("exerciseData", exerciseData)
                )
            }
        } else {
            holder.mCardView.setOnClickListener(null)
        }
    }

    //Interface for passing Data to Activity
    interface workoutCompleted {
        fun passResultCallback(isWorkoutCompleted: Boolean)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun markAsChecked(exerciseName: String) {
        mCheckedExercises.add(exerciseName)
    }

    fun addItem(
        _exerciseNameAdd: String,
        _weightAdd: Float,
        _repsAdd: Int,
        _setsAdd: Int,
        _imageAdd: String
    ) {
        exercises.add(_exerciseNameAdd)
        weights.add(_weightAdd)
        reps.add(_repsAdd)
        sets.add(_setsAdd)
        images.add(_imageAdd)

        notifyItemInserted(exercises.size - 1)
    }

    fun deleteItem(position: Int, holder: ExerciseViewHolder, showToast: Boolean) {
        for ((index, value) in mCheckedExercises.withIndex()) {
            if (value == exercises[index]) {
                mCheckedExercises.removeAt(index)
            }
        }
        Log.e("*********", "$exercises")
        exercises.removeAt(position)
        weights.removeAt(position)
        reps.removeAt(position)
        sets.removeAt(position)
        images.removeAt(position)
        notifyItemRemoved(position)
    }
}