package com.datasoft.abs.presenter.view.dashboard.fragments.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.FragmentDashboardBinding
import com.datasoft.abs.presenter.states.Status
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private val viewModel: TransactionViewModel by activityViewModels()
    private var _binding: FragmentDashboardBinding? = null

    @Inject
    lateinit var toastHelper: ToastHelper

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setValueOnPieChart(binding.pieChart, 40, 30, 30)
        setValueOnLineChart(binding.lineChart)

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        viewModel.getDashboardData().observe(viewLifecycleOwner, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        goneProgressBar()
                        result.data?.let { dashBoardResponse ->
                            binding.txtViewOpen.text =
                                DecimalFormat(Constant.BALANCE_FORMAT).format(dashBoardResponse.vmAgentInfos.total_Customer)
                                    .toString()
                            binding.txtViewDeposit.text =
                                DecimalFormat(Constant.BALANCE_FORMAT).format(dashBoardResponse.vmAgentInfos.deposit)
                                    .toString()
                            binding.txtViewWithdraw.text =
                                DecimalFormat(Constant.BALANCE_FORMAT).format(dashBoardResponse.vmAgentInfos.withdraw)
                                    .toString()
                            binding.txtViewBalance.text =
                                DecimalFormat(Constant.BALANCE_FORMAT).format(dashBoardResponse.vmAgentInfos.current_Balance)
                                    .toString()
                        }
                    }
                    Status.ERROR -> {
                        goneProgressBar()
                        result.message?.let { message ->
                            toastHelper.sendToast(message)
                        }
                    }
                    Status.LOADING -> {
                        showProgressBar()
                    }
                }
            }
        })
    }

    private fun setValueOnPieChart(chart_pie: PieChart, approved: Int, pending: Int, onHold: Int) {
        chart_pie.clear()

        val arrayListPie = mutableListOf(
            PieEntry(approved.toFloat(), "Approved", null),
            PieEntry(pending.toFloat(), "Pending", null),
            PieEntry(onHold.toFloat(), "On Hold", null)
        )

        //pie chart enable
        chart_pie.description.isEnabled = false
        chart_pie.isDrawHoleEnabled = false
        chart_pie.isRotationEnabled = false
        chart_pie.rotationAngle = 270f

        val l: Legend = chart_pie.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.formSize = 10f
        l.setDrawInside(false)
        l.textSize = 13f
        l.xEntrySpace = 20f
        l.yEntrySpace = 10f
        l.yOffset = 10f

        chart_pie.setDrawEntryLabels(false)
        chart_pie.setEntryLabelColor(Color.BLACK)
        chart_pie.extraLeftOffset = 20f
        chart_pie.extraRightOffset = 20f
        chart_pie.extraTopOffset = 20f

        val colors: MutableList<Int> = java.util.ArrayList()
        colors.add(ColorTemplate.rgb("#3996CD"))
        colors.add(ColorTemplate.rgb("#EC9D36"))
        colors.add(ColorTemplate.rgb("#A5A5A5"))

        val dataSet = PieDataSet(arrayListPie, "")
        dataSet.setDrawValues(false)
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueLineColor = ColorTemplate.COLOR_NONE
        dataSet.valueTextColor = Color.BLUE
        dataSet.sliceSpace = 3.0f
        dataSet.selectionShift = 10f
        dataSet.colors = colors

        dataSet.valueLinePart1OffsetPercentage = 90f
        dataSet.valueLinePart1Length = 0.6f
        dataSet.valueLinePart2Length = 0.0f

        val dataPie = PieData(dataSet)
        chart_pie.data = dataPie
    }

    private fun setValueOnLineChart(chart: LineChart) {
        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(true)

        chart.dragDecelerationFrictionCoef = 0.9f

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.isHighlightPerDragEnabled = true

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true)

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE)
        chart.animateX(1500)

        // get the legend (only possible after setting data)
        val l = chart.legend

        // modify the legend ...
        l.form = LegendForm.LINE
//        l.typeface = tfLight
        l.textSize = 11f
        l.textColor = Color.BLACK
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
//        l.setYOffset(11f)

        val xAxis = chart.xAxis
//        xAxis.typeface = tfLight
        xAxis.textSize = 11f
        xAxis.textColor = Color.WHITE
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        val leftAxis = chart.axisLeft
//        leftAxis.typeface = tfLight
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMaximum = 200f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

        val rightAxis = chart.axisRight
//        rightAxis.typeface = tfLight
        rightAxis.textColor = Color.RED
        rightAxis.axisMaximum = 900F
        rightAxis.axisMinimum = -200F
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawZeroLine(false)
        rightAxis.isGranularityEnabled = false

        setData(chart, 10, 50F)
    }

    private fun setData(chart: LineChart, count: Int, range: Float) {

        val values1: ArrayList<Entry> = ArrayList()

        for (i in 0 until count) {
            val value = (Math.random() * (range / 2f)).toFloat() + 50
            values1.add(Entry(i.toFloat(), value))
        }
        val values2: ArrayList<Entry> = ArrayList()

        for (i in 0 until count) {
            val value = (Math.random() * range).toFloat() + 450
            values2.add(Entry(i.toFloat(), value))
        }
        val values3: ArrayList<Entry> = ArrayList()
        for (i in 0 until count) {
            val value = (Math.random() * range).toFloat() + 500
            values3.add(Entry(i.toFloat(), value))
        }
        val set1: LineDataSet
        val set2: LineDataSet
        val set3: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set2 = chart.data.getDataSetByIndex(1) as LineDataSet
            set3 = chart.data.getDataSetByIndex(2) as LineDataSet
            set1.values = values1
            set2.values = values2
            set3.values = values3
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values1, "Approved")
            set1.axisDependency = AxisDependency.LEFT
            set1.color = ColorTemplate.getHoloBlue()
            set1.setCircleColor(Color.BLACK)
            set1.lineWidth = 2f
            set1.circleRadius = 3f
            set1.fillAlpha = 65
            set1.fillColor = ColorTemplate.getHoloBlue()
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.setDrawCircleHole(false)
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a dataset and give it a type
            set2 = LineDataSet(values2, "Pending")
            set2.axisDependency = AxisDependency.RIGHT
            set2.color = Color.RED
            set2.setCircleColor(Color.BLACK)
            set2.lineWidth = 2f
            set2.circleRadius = 3f
            set2.fillAlpha = 65
            set2.fillColor = Color.RED
            set2.setDrawCircleHole(false)
            set2.highLightColor = Color.rgb(244, 117, 117)
            //set2.setFillFormatter(new MyFillFormatter(900f));
            set3 = LineDataSet(values3, "On Hold")
            set3.axisDependency = AxisDependency.RIGHT
            set3.color = Color.YELLOW
            set3.setCircleColor(Color.BLACK)
            set3.lineWidth = 2f
            set3.circleRadius = 3f
            set3.fillAlpha = 65
            set3.fillColor = ColorTemplate.colorWithAlpha(Color.YELLOW, 200)
            set3.setDrawCircleHole(false)
            set3.highLightColor = Color.rgb(244, 117, 117)

            // create a data object with the data sets
            val data = LineData(set1, set2, set3)
            data.setValueTextColor(Color.BLACK)
            data.setValueTextSize(9f)

            // set data
            chart.data = data
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goneProgressBar() {
        binding.loaderView.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loaderView.visibility = View.VISIBLE
    }
}