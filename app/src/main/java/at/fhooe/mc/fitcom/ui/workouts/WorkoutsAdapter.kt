package at.fhooe.mc.fitcom.ui.workouts

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.ui.exercises.ExerciseActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WorkoutsAdapter(var workoutNames: ArrayList<String>, var workoutColors: ArrayList<String>): RecyclerView.Adapter<WorkoutsViewHolder>() {

    private var mAuth = FirebaseAuth.getInstance()
    private var mDbCollection =
        Firebase.firestore.collection("users").document(mAuth.uid.toString())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutsViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.workout_item, null)
            return WorkoutsViewHolder(root)
        }
    }

    override fun onBindViewHolder(holder: WorkoutsViewHolder, position: Int) {
        holder.mWorkoutName.text = workoutNames[position]
        holder.mRectangle.background.setTint(workoutColors[position].toInt())

        holder.mDeleteButton.setOnClickListener {
            mDbCollection.collection("exercises").document(workoutNames[position]).delete()
            workoutNames.removeAt(position)
            workoutColors.removeAt(position)

            notifyItemRemoved(position)
        }

        holder.mRectangle.setOnClickListener {
            holder.mRoot.context.startActivity(Intent(holder.mRoot.context, ExerciseActivity::class.java).putExtra("workout", workoutNames[position]))
        }
    }

    override fun getItemCount(): Int {
        return workoutNames.size
    }

    fun addItem(_workoutNameAdd: String, _workoutColorAdd: String) {
        workoutNames.add(_workoutNameAdd)
        workoutColors.add(_workoutColorAdd)
        notifyItemInserted(workoutNames.size-1)
    }

}