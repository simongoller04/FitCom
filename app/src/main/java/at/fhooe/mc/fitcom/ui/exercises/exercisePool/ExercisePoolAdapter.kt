package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R

class ExercisePoolAdapter(var exercisePoolData: ArrayList<ExercisePoolData>): RecyclerView.Adapter<ExercisePoolViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisePoolViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.exercise_pool_item, null)
            return ExercisePoolViewHolder(root)
        }
    }

    override fun onBindViewHolder(holder: ExercisePoolViewHolder, position: Int) {
        holder.mExerciseName.text = exercisePoolData[position].name
        holder.mExerciseCategory.text = getCategory(exercisePoolData[position].category)

//        holder.mRectangle.setOnClickListener {
//            TODO("Open dialog to add weigth, reps and sets")
//        }
    }

    override fun getItemCount(): Int {
        return exercisePoolData.size
    }

    private fun getCategory(category: Int): String{
        when(category){
            10 -> return ("Core")
            8 -> return ("Arms")
            12 -> return ("Back")
            14 -> return ("Abs")
            11 -> return ("Chest")
            9 -> return ("Legs")
            13 -> return ("Shoulder")
        }
        return "*Undefined*"
    }

}