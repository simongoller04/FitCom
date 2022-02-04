package at.fhooe.mc.fitcom.ui.stats

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.FragmentStatsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    lateinit var lineChart: LineChart

    lateinit var entries: ArrayList<Entry>
    lateinit var lineDataSet: LineDataSet
    lateinit var lineData: LineData

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(StatsViewModel::class.java)

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // general settings for the chart
        lineChartSetup(root)

        // Enter data into the chart
        entries = ArrayList<Entry>()
        entries.add(Entry(1f, 65.5f))
        entries.add(Entry(2f, 66f))
        entries.add(Entry(3f, 66.1f))
        entries.add(Entry(4f, 65.8f))
        entries.add(Entry(5f, 66.1f))
        entries.add(Entry(6f, 65.1f))
        entries.add(Entry(7f, 65.1f))
        entries.add(Entry(8f, 66.5f))
        entries.add(Entry(9f, 65.0f))

        lineDataSet = LineDataSet(entries, "Entries")
        lineData = LineData(lineDataSet)

        lineChart.data = lineData

        styleLineDataSet(root.context)

        var floatingActionButton: FloatingActionButton = root.findViewById(R.id.naviagtion_stats_floatingActionButton)
        floatingActionButton.setOnClickListener {
            showAddBodyWeightDialog()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Helper method for the setup of the chart
     */
    private fun lineChartSetup (view: View) {

        lineChart = view.findViewById(R.id.lineChart)

        lineChart.axisLeft.isEnabled = false

        lineChart.axisRight.apply {
            isEnabled = true
            textColor = Color.WHITE
            textSize = 15f
            setDrawGridLines(false)
            setDrawAxisLine(false)
        }

        lineChart.xAxis.apply {
            setDrawAxisLine(false)
            textColor = Color.WHITE
            textSize = 15f
            axisMinimum = 1f
            isGranularityEnabled = true
            setDrawGridLines(false)
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
            setDrawHighlightIndicators(false)
            setDrawCircles(false)

            // disable the labels of every entry
            setDrawValues(false)

            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(context, R.drawable.line_gradient)
        }
    }

    /**
     * Helper method to open up a dialog window
     */
    private fun showAddBodyWeightDialog() {
        val dialog = Dialog(binding.root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_body_weight)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }
}