package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R

class ExercisePoolAdapter(var exercises: ArrayList<String>, var icons: ArrayList<Drawable>): RecyclerView.Adapter<ExercisePoolViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisePoolViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.exercise_pool_item, null)
            return ExercisePoolViewHolder(root)
        }
    }

    override fun onBindViewHolder(holder: ExercisePoolViewHolder, position: Int) {
        holder.mExerciceName.text = exercises[position]
        holder.mIcon.setImageDrawable(icons[position])

        holder.mRectangle.setOnClickListener {
            TODO("Open dialog to add weigth, reps and sets")
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

}