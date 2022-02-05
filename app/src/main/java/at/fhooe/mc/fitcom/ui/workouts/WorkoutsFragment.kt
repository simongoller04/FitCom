package at.fhooe.mc.fitcom.ui.workouts

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.FragmentWorkoutsBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class WorkoutsFragment : Fragment() {

    private var _binding: FragmentWorkoutsBinding? = null
    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = Firebase.firestore
    private var mWorkoutNames = ArrayList<String>()
    private var mWorkoutColors = ArrayList<String>()
    private lateinit var mAdapter: WorkoutsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(WorkoutsViewModel::class.java)

        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)

        binding.fragmentWorkoutsFabAddWorkout.setOnClickListener {
            showSectionColorDialog()
        }

        if (mAuth.currentUser != null) {

            mDb.collection("users").document(mAuth.uid.toString()).get().addOnCompleteListener() {
                if (it.isSuccessful) {

                    mWorkoutNames = it.result!!["workoutNames"] as ArrayList<String>
                    mWorkoutColors = it.result!!["workoutColors"] as ArrayList<String>

                    binding.fragmentWorkoutsTextView.isVisible = mWorkoutNames.isEmpty()

                    mAdapter = WorkoutsAdapter(mWorkoutNames, mWorkoutColors)

                    binding.fragmentsWorkoutsRecyclerView.adapter = mAdapter
                    binding.fragmentsWorkoutsRecyclerView.layoutManager =
                        LinearLayoutManager(binding.root.context)


                } else {
                    Toast.makeText(binding.root.context, "Fetching Data failed!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }



        return binding.root
    }


    override fun onPause() {
        super.onPause()
        //add workout to firestore
        mDb.collection("users").document(mAuth.uid.toString()).update(
            "workoutNames",
            mWorkoutNames,
            "workoutColors",
            mWorkoutColors
        )}


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSectionColorDialog() {
        val dialog = Dialog(binding.root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_workout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val color1 = Color.BLACK
        val color2 = Color.GRAY
        val color3 = Color.parseColor("#4B7BF2")
        val color4 = Color.parseColor("#8AE043")
        val color5 = Color.parseColor("#F7CE3E")
        val color6 = Color.parseColor("#F07F4B")

        val cardView1 =
            dialog.findViewById(R.id.dialog_add_workout_cardView1) as MaterialCardView
        val cardView2 =
            dialog.findViewById(R.id.dialog_add_workout_cardView2) as MaterialCardView
        val cardView3 =
            dialog.findViewById(R.id.dialog_add_workout_cardView3) as MaterialCardView
        val cardView4 =
            dialog.findViewById(R.id.dialog_add_workout_cardView4) as MaterialCardView
        val cardView5 =
            dialog.findViewById(R.id.dialog_add_workout_cardView5) as MaterialCardView
        val cardView6 =
            dialog.findViewById(R.id.dialog_add_workout_cardView6) as MaterialCardView
        val cancelButton = dialog.findViewById(R.id.dialog_add_workout_button_cancel) as Button
        val addButton = dialog.findViewById(R.id.dialog_add_workout_button_add) as Button
        val textInputLayout =
            dialog.findViewById(R.id.dialog_add_workout_textInputLayout_workoutName) as TextInputLayout

        cardView1.setCardBackgroundColor(color1)
        cardView2.setCardBackgroundColor(color2)
        cardView3.setCardBackgroundColor(color3)
        cardView4.setCardBackgroundColor(color4)
        cardView5.setCardBackgroundColor(color5)
        cardView6.setCardBackgroundColor(color6)

        cardView1.isChecked = true

        var workoutColor = color1

        fun unselectAllCardViews() {
            cardView1.isChecked = false
            cardView2.isChecked = false
            cardView3.isChecked = false
            cardView4.isChecked = false
            cardView5.isChecked = false
            cardView6.isChecked = false
        }

        cardView1.setOnClickListener {
            unselectAllCardViews()
            cardView1.isChecked = true
            workoutColor = color1
        }

        cardView2.setOnClickListener {
            unselectAllCardViews()
            cardView2.isChecked = true
            workoutColor = color2
        }

        cardView3.setOnClickListener {
            unselectAllCardViews()
            cardView3.isChecked = true
            workoutColor = color3
        }

        cardView4.setOnClickListener {
            unselectAllCardViews()
            cardView4.isChecked = true
            workoutColor = color4
        }

        cardView5.setOnClickListener {
            unselectAllCardViews()
            cardView5.isChecked = true
            workoutColor = color5
        }

        cardView6.setOnClickListener {
            unselectAllCardViews()
            cardView6.isChecked = true
            workoutColor = color6
        }

        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        addButton.setOnClickListener {
            val workoutName = textInputLayout.editText?.text
            if (workoutName.isNullOrEmpty()) {
                textInputLayout.error = "Add a workout name"
            } else {

                binding.fragmentWorkoutsTextView.isVisible = false

                mAdapter.addItem(workoutName.toString(), workoutColor.toString())
                dialog.cancel()
                Toast.makeText(
                    binding.root.context,
                    "Added workout $workoutName!",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

        dialog.show()

    }
}