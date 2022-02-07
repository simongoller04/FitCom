package at.fhooe.mc.fitcom.ui.stats

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.FragmentStatsBinding
import at.fhooe.mc.fitcom.ui.workouts.WorkoutsAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList


class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var lineChart: LineChart
    lateinit var entries: ArrayList<Entry>
    lateinit var lineDataSet: LineDataSet
    lateinit var lineData: LineData

    lateinit var currentWeight: TextView
    lateinit var gainedWeight: TextView
    private var weight: Float = 0.0f
    private var prevWeight: Float = 0.0f

    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = Firebase.firestore

    private var mWeightArray = ArrayList<Float>()
    private var mCurrentWeight = 0f
    private var mPrevWeight = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(StatsViewModel::class.java)

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Enter data into the chart
        entries = ArrayList<Entry>()

        if (mAuth.currentUser != null) {

            mDb.collection("users").document(mAuth.uid.toString()).get().addOnCompleteListener() {
                if (it.isSuccessful) {

                    mWeightArray = it.result!!["weightArray"] as ArrayList<Float>
                    mPrevWeight = (it.result!!["weightPrev"] as Double).toFloat()
                    mCurrentWeight = (it.result!!["weight"] as Double).toFloat()
                    binding.statsWorkoutTotalKgLiftedAmountTextview.text = (it.result!!["totalWeightLifted"] as Double).toInt().toString()
                    binding.statsWorkoutsCompletedAmountTextview.text = (it.result!!["workoutsCompleted"] as Long).toString()

                    weight = mCurrentWeight
                    prevWeight = mPrevWeight

                    mWeightArray.forEach{
                        lineDataSet.addEntry(Entry(entries.size.toFloat(), it))
                    }

                    binding.statsCurrentWeightAmountTextview.text = mCurrentWeight.toString() + " kg"
                    val gainedAmount = mCurrentWeight - mPrevWeight
                    if (gainedAmount < 0){
                        binding.statsGainedAmountTextview.text = gainedAmount.toString() + " kg"
                    }else{
                        binding.statsGainedAmountTextview.text = "+" + gainedAmount.toString() + " kg"
                    }


                    lineData.notifyDataChanged()
                    lineChart.notifyDataSetChanged()
                    lineChart.invalidate()

                } else {
                    Toast.makeText(binding.root.context, "Fetching Data failed!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        // general settings for the chart
        lineChartSetup(root)

        lineDataSet = LineDataSet(entries, "Entries")
        lineData = LineData(lineDataSet)

        lineChart.data = lineData

        styleLineDataSet(root.context)

        var floatingActionButton: FloatingActionButton = root.findViewById(R.id.naviagtion_stats_floatingActionButton)
        floatingActionButton.setOnClickListener {
            showAddBodyWeightDialog(root)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Helper method for the setup of the Chart
     */
    private fun lineChartSetup (view: View) {

        lineChart = view.findViewById(R.id.fragment_stats_lineChart)
        lineChart.extraBottomOffset = 5f

        lineChart.axisLeft.isEnabled = false

        lineChart.axisRight.apply {
            isEnabled = true
            textColor = Color.WHITE
            textSize = 15f
            granularity = 0.1f
            setDrawGridLines(false)
            setDrawAxisLine(false)
        }

        lineChart.xAxis.apply {
            setDrawAxisLine(false)
            textColor = Color.WHITE
            textSize = 15f

            Log.e("*'***********", "${entries.size}")
            axisMinimum = 0F
            isGranularityEnabled = true
            granularity = 1f
            setDrawGridLines(false)
            labelCount = 7

            position = XAxis.XAxisPosition.BOTTOM
        }

        lineChart.apply {
            description = null
            legend.isEnabled = false

            setTouchEnabled(true)
        }
    }

    /**
     * Helper method to style the line of the graph
     */
    private fun styleLineDataSet(context: Context) {
        lineDataSet.apply {
            setColors(Color.WHITE)
            valueTextColor = Color.BLUE
            valueTextSize = 16f
            lineWidth = 3f
            isHighlightEnabled = true
            setDrawHighlightIndicators(true)
            setDrawCircles(true)
            setCircleColor(Color.WHITE)

            // disable the labels of every entry
            setDrawValues(false)

            //round graph
//            mode = LineDataSet.Mode.CUBIC_BEZIER

            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(context, R.drawable.line_gradient)
        }
    }

    /**
     * Helper method to open up a dialog window
     */
    private fun showAddBodyWeightDialog(view: View) {
        val dialog = Dialog(binding.root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_body_weight)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val addButton: Button = dialog.findViewById(R.id.dialog_add_body_weight_button_add)
        val cancelButton: Button = dialog.findViewById(R.id.dialog_add_body_weight_button_cancle)
        val weightText: EditText = dialog.findViewById(R.id.dialog_add_body_weight_text)

        currentWeight = view.findViewById(R.id.stats_current_weight_amount_textview)
        gainedWeight = view.findViewById(R.id.stats_gained_amount_textview)

        // show the keyboard when opening the dialog
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        weightText.text = prevWeight.toString().toEditable()

        addButton.setOnClickListener {

            mPrevWeight = weight
            weight = weightText.text.toString().toFloat()

            mCurrentWeight = weight
            mWeightArray.add(weight)

            var currentString = "$weight kg"
            currentWeight.text = currentString

            lineDataSet.addEntry(Entry(entries.size.toFloat(), weight))

            lineData.notifyDataChanged()
            lineChart.notifyDataSetChanged()
            lineChart.invalidate()

            var gained: Float = 0f
            if (entries.size > 1) {
                gained = String.format("%.1f", weight - prevWeight).toFloat()
            }

            var gainedString: String = "$gained kg"
            if (gained > 0) {
                gainedString = "+$gained kg"
            }

            gainedWeight.text = gainedString
            prevWeight = weight

            // hide keyboard
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            // hide keyboard
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        mDb.collection("users").document(mAuth.uid.toString()).update(
            "weightArray",
            mWeightArray,
            "weight", mCurrentWeight,
            "weightPrev", mPrevWeight
        )
    }

    /**
     * Helper function to format a String into an Editable
     */
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}