package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.rotationMatrix
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R

class ExercisePoolViewHolder(root: View): RecyclerView.ViewHolder(root){

    val mExerciceName: TextView = root.findViewById(R.id.exercise_pool_textView)
    val mRectangle: View = root.findViewById(R.id.exercise_pool_rectangle)
    val mIcon: ImageView = root.findViewById(R.id.exercise_pool_icon)


}