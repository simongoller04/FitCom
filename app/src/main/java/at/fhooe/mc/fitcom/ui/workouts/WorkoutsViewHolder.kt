package at.fhooe.mc.fitcom.ui.workouts

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R

class WorkoutsViewHolder (root: View): RecyclerView.ViewHolder(root) {

    val mWorkoutName: TextView = root.findViewById(R.id.workouts_item_text)
    val mRectangle: View = root.findViewById(R.id.workouts_list_item_rectangle)
    val mDeleteButton: ImageButton = root.findViewById(R.id.workouts_item_delete_button)
    val mRoot: View = root

}