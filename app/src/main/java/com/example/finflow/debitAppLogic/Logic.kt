package com.example.finflow.debitAppLogic

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class Logic {
    fun currentDateAndTime():String{
        //Get the current Date
        val current : Date = Calendar.getInstance().time
        //Format this date and time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formattedDate = dateFormat.format(current).toString()
        return formattedDate
    }

    fun formatAmountInCrores(amount: Float): String {
        val crores = amount / 10000000.0
        return String.format("%.4f cr", crores)
    }

    fun rateCalculator(presentPercent : Float, expectedPercent: Float): Float{
        var rate: Float
        var lowerBound = 20.0F
        if(expectedPercent < lowerBound)
            lowerBound = expectedPercent
      //  Log.e("Rate calculator","Lower bound = $lowerBound, expectedPercent= $expectedPercent, presentPercent = $presentPercent")
        if(presentPercent > (expectedPercent)  && presentPercent < (expectedPercent + 20)){
            rate = 350.0F - (presentPercent - expectedPercent)*(350.0F - 50.0F)/20.0F
        }else if(presentPercent < expectedPercent && presentPercent > (expectedPercent-lowerBound)) {
            rate = 350.0F + (750.0F - 350.0F)*(expectedPercent - presentPercent)/lowerBound
        }else if(presentPercent >= (expectedPercent + 20))
            rate = 50.0F
        else if(presentPercent <= (expectedPercent - lowerBound))
            rate = 750.0F
        else
            rate = 350.0F
        return rate
    }

    fun formatFloatToTwoDecimalPlaces(value: Float): String {
        // Use String.format to format the float to two decimal places
        return String.format("%.2f", value)
    }

    fun recoveryTimeCalculator(expectedPercent: Float, presentTimeSpent: Float, totalTimeSpent: Float): String{
        if(presentTimeSpent/totalTimeSpent *100.0F >= expectedPercent)
            return ""
        val recoveryTime = -(totalTimeSpent* expectedPercent - presentTimeSpent*100.0F)/ (expectedPercent - 100.0F)
        return "Recovery time = ${formatFloatToTwoDecimalPlaces(recoveryTime)} min"
    }

    fun convertMinutesToHoursString(minutes: Long): String {
        try {
            val hours = minutes.toDouble() / 60
            val formattedHours = "%.2f".format(hours)
            return "$formattedHours hrs"
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Please provide a valid numeric input for minutes.")
        }
    }

    fun main(){
        println(rateCalculator(100.0F,55.0F))
    }

}