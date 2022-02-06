package at.fhooe.mc.fitcom.ui.exercises.exercisePool

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R
import com.bumptech.glide.Glide

class ExercisePoolAdapter(var exercisePoolData: ArrayList<ExercisePoolData>, var exercisePoolImages: ArrayList<ExercisePoolImagesData>): RecyclerView.Adapter<ExercisePoolViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisePoolViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.exercise_pool_item, null)
            return ExercisePoolViewHolder(root)
        }
    }

    override fun onBindViewHolder(holder: ExercisePoolViewHolder, position: Int) {
        holder.mExerciseName.text = exercisePoolData[position].name
        holder.mExerciseCategory.text = getCategory(exercisePoolData[position].category)
//        val url: ExercisePoolImagesData? = exercisePoolImages.find{ it.id == exercisePoolData[position].id}
//            if(url != null) {
                val correctId =  getCorrectId(exercisePoolData[position].id)
                val correctUrl: ExercisePoolImagesData? = exercisePoolImages.find{ it.id == correctId}
//                Log.e("*************", "${url.id} + ${url.image} || ${exercisePoolData[position].name} + ${exercisePoolData[position].id}")
                Log.e("**** correct ****", "$correctId + ${correctUrl?.image} || ${exercisePoolData[position].name} + ${exercisePoolData[position].id}")
                if (correctUrl != null){
                    holder.mCardView.isVisible = true
                    Glide.with(holder.itemView)
                        .load(correctUrl.image)
                        .into(holder.mImageView)
                }else {
                    holder.mCardView.isVisible = false
                    holder.mImageView.setImageResource(R.drawable.ic_exercises)
                }
//            }else{
//                holder.mCardView.isVisible = false
//                holder.mImageView.setImageResource(R.drawable.ic_exercises)
//            }

        holder.mCardView.setOnClickListener {
            showAddExerciseDialog(holder)
//            TODO("Open dialog to add weigth, reps and sets")
        }
    }

    override fun getItemCount(): Int {
        return exercisePoolData.size
    }

    private fun showAddExerciseDialog(holder: ExercisePoolViewHolder){
        val dialog = Dialog(holder.itemView.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_exercise)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
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

    private fun getCorrectId(exerciseId: Int): Int?{
        when (exerciseId){
            91 -> return 3
            93 -> return 7
            128 -> return 11
            88 -> return 15
            129 -> return 19
            81 -> return 23
            74 -> return 27
            82 -> return 31
            83 -> return 35
            151 -> return 39
            150 -> return 43
            86 -> return 47
            138 -> return 51
            195 -> return 55
            84 -> return 60
            386 -> return 62
            116 -> return 66
            192 -> return 68
            127 -> return 69
            181 -> return 73
            106 -> return 77
            109 -> return 84
            110 -> return 91
            193 -> return 93
            311 -> return 95
            210 -> return 105
            217 -> return 107
            98 -> return 111
            97 -> return 113
            100 -> return 115
            163 -> return 117
            122 -> return 119
            113 -> return 121
            130 -> return 125
            207 -> return 127
            791 -> return 137
            125 -> return 139
            143 -> return 143
            161 -> return 145
            176 -> return 147
            191 -> return 149
            111 -> return 151
            792 -> return 165
            177 -> return 167
            119 -> return 171
            123 -> return 173
            152 -> return 175
            148 -> return 177
            154 -> return 179
            117 -> return 181
            118 -> return 183
            768 -> return 189
            213 -> return 209
        }
        return null
    }

}