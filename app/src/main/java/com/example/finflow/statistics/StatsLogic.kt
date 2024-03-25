package com.example.finflow.statistics

import com.example.finflow.transactionHistory.TransEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StatsLogic {
    // Function to filter transactions for the last one week
    fun filterTransactions(transactions: List<TransEntity>, days: Int = 7): List<TransEntity> {
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time

        // Calculate the date and time exactly one week ago
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        val oneWeekAgo = calendar.time

        val filteredTransactions = mutableListOf<TransEntity>()

        // Iterate through transactions and filter out those within the last one week
        for (transaction in transactions) {
            val transactionDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(transaction.dateTime)

            if (transactionDateTime in oneWeekAgo..currentDate) {
                filteredTransactions.add(transaction)
            }
        }

        return filteredTransactions
    }

    fun generateDayWiseHoursStudied(transactions: List<TransEntity>, days: Int = 7): ArrayList<Float>{
        val l = filterTransactions(transactions, days)
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dayFormat = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        var mp: MutableMap<String, Float> = mutableMapOf()

        //Initialize the map with all days in the last "days" days set to 0
        for (i in 0 until days) {
            val day = dayFormat.format(calendar.time)
            mp[day] = 0.0F
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        for(et : TransEntity in l){
            if(et.credit){
                val dateTime = dateTimeFormat.parse(et.dateTime)
                val day = dayFormat.format(dateTime)
                var prevVal = mp[day]
                if (prevVal != null) {
                    mp[day]  = prevVal + et.time/(60.0F) // Add the amount to the total credit for that date
                }
            }
        }
        var ans = ArrayList<Float>()
        for(pr in mp){
            ans.add(pr.value)

        }

        ans.reverse()
        return ans
    }

    fun generateHoursStudied(transactions: List<TransEntity>, days: Int = 7, credit: Int): ArrayList<Long>{
        val l = filterTransactions(transactions, days)
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dayFormat = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        var mp : MutableMap<String, Long> = mutableMapOf()

        // Initialize the map with all days in the last "days" days set to 0
        for (i in 0 until days) {
            val day = dayFormat.format(calendar.time)
            mp[day] = 0
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        for(et: TransEntity in l){
            if(credit == 2){
                val dateTime = dateTimeFormat.parse(et.dateTime)
                val day = dayFormat.format(dateTime)
                var prevVal = mp[day]
                if (prevVal != null) {
                    mp[day]  = prevVal + et.amount.toLong() // Add the amount to the total credit for that date
                }
            }
            else if ((et.credit && credit == 1) || (!et.credit && credit == 0)) { // Check if the transaction is a credit transaction
                val dateTime = dateTimeFormat.parse(et.dateTime)
                val day = dayFormat.format(dateTime)
                var prevVal = mp[day]
                if (prevVal != null) {
                    mp[day]  = prevVal + et.amount.toLong() // Add the amount to the total credit for that date
                }
            }
        }
        var ans = ArrayList<Long>()
        for(pr in mp){
            if(credit != 0)
            ans.add(pr.value)
            else
                ans.add(-pr.value)
        }

        ans.reverse()
        return ans
    }

    // Function to generate labels for the last 'days' days
    fun generateLabels(days: Int = 7): List<String> {
        val labels = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        // Iterate through the last 'days' days
        repeat(days) {
            // Move the calendar backwards by one day
            calendar.add(Calendar.DAY_OF_YEAR, -1)

            // Format the date as per your requirement, e.g., "Feb12", "Dec19"
            val formattedDate = SimpleDateFormat("MMMdd", Locale.ENGLISH).format(calendar.time)

            // Add the formatted date to the list of labels
            labels.add(formattedDate)
        }

        // Reverse the list to have labels in chronological order
        labels.reverse()

        return labels
    }


    fun getPresentAndPastDate(n: Int): Pair<String, String> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val calendar = Calendar.getInstance()

        // Get the present date
        val presentDate = dateFormat.format(calendar.time)

        // Calculate the date n days ago
        calendar.add(Calendar.DAY_OF_YEAR, -n-1)
        val pastDate = dateFormat.format(calendar.time)

        return Pair(presentDate, pastDate)
    }

}