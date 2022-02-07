package at.fhooe.mc.fitcom.ui.exercises

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.ui.workouts.WorkoutsViewHolder

class SwipeToDelete(var adapter: ExerciseAdapter, context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private var deleteBackground: ColorDrawable = ColorDrawable(Color.parseColor("#E41E1E"))
    private var deleteIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)!!

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var position = viewHolder.adapterPosition
        adapter.deleteItem(position, viewHolder as ExerciseViewHolder, true)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        val iconMarginDelete = (itemView.height - deleteIcon.intrinsicHeight) / 2

        //swipe left
            deleteBackground.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            deleteIcon.setBounds(
                itemView.right - iconMarginDelete - deleteIcon.intrinsicWidth,
                itemView.top + iconMarginDelete,
                itemView.right - iconMarginDelete,
                itemView.bottom - iconMarginDelete
            )
            deleteBackground.draw(c)
            deleteIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}