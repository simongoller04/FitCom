package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R
import com.bumptech.glide.Glide

class ExercisePoolAdapter(
    var exercisePoolData: ArrayList<ExercisePoolFinalizedData>
) : RecyclerView.Adapter<ExercisePoolViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisePoolViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.exercise_pool_item, null)
            return ExercisePoolViewHolder(root)
        }
    }

    override fun onBindViewHolder(holder: ExercisePoolViewHolder, position: Int) {
        holder.mExerciseName.text = exercisePoolData[position].name
        holder.mExerciseCategory.text = getCategory(exercisePoolData[position].category)
        Glide.with(holder.itemView)
                .load(exercisePoolData[position].image)
                .into(holder.mImageView)

        holder.mCardView.setOnClickListener {
            showAddExerciseDialog(holder, exercisePoolData[position].image, exercisePoolData[position].id)
//            TODO("Open dialog to add weigth, reps and sets")
        }
    }

    override fun getItemCount(): Int {
        return exercisePoolData.size
    }

    private fun showAddExerciseDialog(
        holder: ExercisePoolViewHolder,
        image: String,
        exerciseId: Int
    ) {
        val dialog = Dialog(holder.itemView.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_exercise)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val imageView: ImageView = dialog.findViewById(R.id.dialog_add_exercise_imageView)
        val cancelBtn: Button = dialog.findViewById(R.id.dialog_add_exercise_button_cancel)
        val addBtn: Button = dialog.findViewById(R.id.dialog_add_exercise_button_add)
        val infoBtn: Button = dialog.findViewById(R.id.dialog_add_exercise_button_info)

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        addBtn.setOnClickListener {
            //TODO add data to firebase
            holder.mCardView.isChecked = true
            dialog.dismiss()
        }

        infoBtn.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://wger.de/en/exercise/$exerciseId/view")
                )
            )
        }

        Glide.with(holder.itemView)
            .load(image)
            .into(imageView)

        dialog.show()
    }

    private fun getCategory(category: Int): String {
        when (category) {
            10 -> return ("Core")
            8 -> return ("Arms")
            12 -> return ("Back")
            14 -> return ("Abs")
            11 -> return ("Chest")
            9 -> return ("Legs")
            13 -> return ("Shoulders")
        }
        return "*Undefined*"
    }

}