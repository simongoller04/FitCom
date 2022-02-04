package at.fhooe.mc.fitcom.ui.exercises

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R

class ExerciseViewHolder (root: View): RecyclerView.ViewHolder(root) {

    val mExerciseName: TextView = root.findViewById(R.id.exercise_item_text)
    val mWeight: TextView = root.findViewById(R.id.exercise_item_weight)
    val mReps: TextView = root.findViewById(R.id.exercise_item_reps)
    val mSets: TextView = root.findViewById(R.id.exercise_item_sets)
    val mEditButton: ImageButton = root.findViewById(R.id.exercise_item_button)
    val mRectangle: View = root.findViewById(R.id.exercise_item_rectangle)
    val mRoot: View = root
}