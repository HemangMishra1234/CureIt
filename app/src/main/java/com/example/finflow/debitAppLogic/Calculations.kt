package com.example.finflow.debitAppLogic

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.widget.Toast
import kotlinx.coroutines.currentCoroutineContext

class Calculations(private val context: Context) {

    val SHARED_PREF_NAME = "Balance Shared Pref"
    val SHARED_VALUE = "Balance"

    fun saveNewBalanceInSharedPref(balance: Float) {
        //Opening the sharedpref

        val sharedPref: SharedPreferences =
            context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)

        Toast.makeText(context,"Balance is ${Logic().formatAmountInCrores(balance)}", Toast.LENGTH_SHORT).show()

        //writing data into shared preferences
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putFloat(SHARED_VALUE, balance)
        editor.commit()
    }

    fun getBalanceInSharedPref(): Float{
        //Opening the sharedpref


        val sharedPref: SharedPreferences =
            context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)

        return  sharedPref.getFloat(SHARED_VALUE, 0.0F)
    }

    fun debitCalculations(timeInMin: Long, rate:Float): Float{
        var present = getBalanceInSharedPref()
        var debitAmt = (timeInMin * 60000 * rate / 100.0F );
        present -= debitAmt ;
        Toast.makeText(context,"Debit of Rs.${Logic().formatAmountInCrores(debitAmt)}", Toast.LENGTH_SHORT).show()
        saveNewBalanceInSharedPref(present)
        return -((timeInMin * 60000 * rate / 100.0F ))
    }

    fun creditCalculations(timeInMin: Long, rate:Float): Float{
        var present = getBalanceInSharedPref()
        present += (timeInMin * 60000 * rate / 100.0F )
        var creditAmt = timeInMin * 60000 * rate / 100.0F
        Toast.makeText(context,"Credit of Rs. ${Logic().formatAmountInCrores(creditAmt)}", Toast.LENGTH_SHORT).show()
        saveNewBalanceInSharedPref(present)
        return (timeInMin * 60000 * rate / 100.0F )
    }

    fun updateTransactionInSP(rate:Float,timeInMin:Long,credit:Boolean,lastAmt:Float):Float{
        var newMag = (timeInMin * 60000 * rate / 100.0F );
        if(!credit)
            newMag = -newMag;
        saveNewBalanceInSharedPref(getBalanceInSharedPref() + newMag - lastAmt)
        return newMag
    }

    fun calculationsWithoutSaving(timeInMin: Long, rate:Float, credit:Boolean): Float{
        if(credit)
        return (timeInMin * 60000 * rate / 100.0F )
        else
            return -(timeInMin * 60000 * rate / 100.0F )
    }
}
