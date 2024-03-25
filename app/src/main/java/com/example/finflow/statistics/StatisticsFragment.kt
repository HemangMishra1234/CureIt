package com.example.finflow.statistics

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.finflow.R
import com.example.finflow.databinding.FragmentStatisticsBinding
import com.example.finflow.databinding.FragmentTransactionBinding
import com.example.finflow.transactionHistory.TransEntity
import com.example.finflow.transactionHistory.TransactionDatabase
import com.example.finflow.transactionHistory.TransactionRepository
import com.example.finflow.transactionHistory.TransactionViewModel
import com.example.finflow.transactionHistory.TransactionViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Type for statistics. 1 for Last 7 days.
class StatisticsFragment( val type: Int = 7) : Fragment() {

    private lateinit var viewModel: StatisticsViewModel
    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)

        //Room
        val dao = TransactionDatabase.getInstance(requireContext().applicationContext).transDAOObject
        val repository = TransactionRepository(dao)
        val factory = StatisticsViewModelFactory(repository, type)
        viewModel = ViewModelProvider(this,factory).get(StatisticsViewModel::class.java)

        val barChartCre: BarChart = binding.workTargetGraph
        val barChartDeb: BarChart = binding.debitGraph
        val barChartNet: BarChart = binding.netGraph
        val barChartHr: BarChart =  binding.hoursWorkedgraph

       // binding.statisticsViewModel = viewModel
       // binding.lifecycleOwner = activity

        viewModel.allTrans.observe(viewLifecycleOwner) { transactions ->

            CoroutineScope(Dispatchers.Default).launch {
                presentBarChart1(barChartCre, transactions)
            }
            CoroutineScope(Dispatchers.Default).launch {
                presentBarChart1(barChartDeb, transactions, 0)
            }
            CoroutineScope(Dispatchers.Default).launch {
                presentBarChart1(barChartNet, transactions, 2)
            }
            CoroutineScope(Dispatchers.Default).launch {
                presentBarChartHours(barChartHr, transactions)
            }
            viewModel.initData()
        }

// Change the text color of the XAxis, YAxis (both left and right), and Legend

    // Change this to your desired color

//// For the credit graph
//        barChartCre.xAxis.textColor = color
//        barChartCre.axisLeft.textColor = color
//        barChartCre.axisRight.textColor = color
//        barChartCre.legend.textColor = color
//        barChartCre.description = null
//
//
//// For the debit graph





        return binding.root
    }

    fun presentBarChart1(barChart: BarChart, allTrans: List<TransEntity>?, credit: Int = 1){
        var entries = ArrayList<BarEntry>()
        val color = ContextCompat.getColor(requireContext(), R.color.pink)
        if(allTrans == null){
            Toast.makeText(activity, "No Transactions", Toast.LENGTH_SHORT).show()
        }else{
            val dayWise = StatsLogic().generateHoursStudied(allTrans, type, credit)
            val labels = StatsLogic().generateLabels(type)
            var i: Float = 1f
            for(et in dayWise)
            {
                entries.add(BarEntry(i++, et/10000000f))
            }
            val xAxis = barChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawLabels(true)
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)

            val dataset = BarDataSet(entries,"Work")
            dataset.valueTextColor = color
            val data = BarData(dataset)

            barChart.data = data
        }
        val color2 = ContextCompat.getColor(requireContext(), R.color.pink)

        barChart.xAxis.textColor = color2
        barChart.axisLeft.textColor = color2
        barChart.axisRight.textColor = color2
        barChart.legend.textColor = color2
        barChart.description = null

        barChart.invalidate()

    }

    fun presentBarChartHours(barChart: BarChart, allTrans: List<TransEntity>?, credit: Int = 1){
        var entries = ArrayList<BarEntry>()
        val color = ContextCompat.getColor(requireContext(), R.color.pink)
        if(allTrans == null){
            Toast.makeText(activity, "No Transactions", Toast.LENGTH_SHORT).show()
        }else{
            var dayWise = StatsLogic().generateDayWiseHoursStudied(allTrans, type)
            val labels = StatsLogic().generateLabels(type)
            var i: Float = 1f
            for(et in dayWise)
            {
                if(et > 15) entries.add(BarEntry(i++, 15.0F))
                else entries.add(BarEntry(i++, et))
            }
            val xAxis = barChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawLabels(true)
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)

            val dataset = BarDataSet(entries,"Work")
            dataset.valueTextColor = color
            val data = BarData(dataset)

            barChart.data = data
        }
        val color2 = ContextCompat.getColor(requireContext(), R.color.pink)

        barChart.xAxis.textColor = color2
        barChart.axisLeft.textColor = color2
        barChart.axisRight.textColor = color2
        barChart.legend.textColor = color2
        barChart.description = null

        barChart.invalidate()

    }

}