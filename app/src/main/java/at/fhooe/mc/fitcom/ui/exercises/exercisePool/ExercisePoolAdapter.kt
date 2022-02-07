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
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout

class ExercisePoolAdapter(
    var exercisePoolData: ArrayList<ExercisePoolFinalizedData>,
    private val callBackInterface: CallBackInterface
) : RecyclerView.Adapter<ExercisePoolViewHolder>() {

    val mCheckedArray = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisePoolViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.exercise_pool_item, null)
            return ExercisePoolViewHolder(root)
        }
    }

    override fun onBindViewHolder(holder: ExercisePoolViewHolder, position: Int) {
        holder.mCardView.isChecked = mCheckedArray.contains(exercisePoolData[position].name)
        holder.mExerciseName.text = exercisePoolData[position].name
        holder.mExerciseCategory.text = getCategory(exercisePoolData[position].category)
        Glide.with(holder.itemView)
            .load(exercisePoolData[position].image)
            .into(holder.mImageView)

        holder.mCardView.setOnClickListener {
            showAddExerciseDialog(
                holder,
                exercisePoolData[position].image,
                exercisePoolData[position].id,
                exercisePoolData[position].name
            )
        }
    }

    //Interface for passing Data to Activity
    interface CallBackInterface {
        fun passResultCallback(exerciseData: SingleCompleteExerciseData)
    }

    override fun getItemCount(): Int {
        return exercisePoolData.size
    }

    private fun showAddExerciseDialog(
        holder: ExercisePoolViewHolder,
        image: String,
        exerciseId: Int,
        exerciseName: String
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
        val sets: TextView = dialog.findViewById(R.id.dialog_add_exercise_textView_sets)
        val reps: TextView = dialog.findViewById(R.id.dialog_add_exercise_textView_reps)
        val weight: TextInputLayout = dialog.findViewById(R.id.dialog_add_exercise_textField_weight)

        val repsPlusButton: Button = dialog.findViewById(R.id.dialog_add_exercise_button_reps_plus)
        val repsMinusButton: Button =
            dialog.findViewById(R.id.dialog_add_exercise_button_reps_minus)
        val setsPlusButton: Button = dialog.findViewById(R.id.dialog_add_exercise_button_sets_plus)
        val setsMinusButton: Button =
            dialog.findViewById(R.id.dialog_add_exercise_button_sets_minus)

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        addBtn.setOnClickListener {
            if (weight.editText?.text.isNullOrEmpty()) {
                weight.error = "Please enter a weight!"
            } else {
                mCheckedArray.add(exerciseName)
                holder.mCardView.isChecked = true
                val completeExercise = SingleCompleteExerciseData(
                    exerciseName,
                    sets.text.toString().toInt(),
                    reps.text.toString().toInt(),
                    weight.editText?.text.toString().toFloat(),
                    image
                )
                callBackInterface.passResultCallback(completeExercise)

                dialog.dismiss()
            }
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

        repsPlusButton.setOnClickListener {
            var amountReps = reps.text.toString().toInt() + 1
            reps.text = amountReps.toString()
        }

        repsMinusButton.setOnClickListener {
            var amountReps = reps.text.toString().toInt() - 1
            reps.text = amountReps.toString()
        }

        setsPlusButton.setOnClickListener {
            var amountSets = sets.text.toString().toInt() + 1
            sets.text = amountSets.toString()
        }

        setsMinusButton.setOnClickListener {
            var amountSets = sets.text.toString().toInt() - 1
            sets.text = amountSets.toString()
        }
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