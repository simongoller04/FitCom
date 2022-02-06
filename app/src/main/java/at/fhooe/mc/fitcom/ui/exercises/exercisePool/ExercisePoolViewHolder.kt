package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R
import com.google.android.material.card.MaterialCardView

class ExercisePoolViewHolder(root: View): RecyclerView.ViewHolder(root){

    val mExerciseName: TextView = root.findViewById(R.id.exercise_pool_item_textView_name)
    val mExerciseCategory: TextView = root.findViewById(R.id.exercise_pool_item_textView_category)
    val mImageView: ImageView = root.findViewById(R.id.exercise_pool_item_imageView)
    val mCardView: MaterialCardView = root.findViewById(R.id.exercise_pool_item_cardview)
}