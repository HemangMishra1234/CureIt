package com.example.finflow.transactionHistory

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.Date

@Entity("TransactionHistory")
data class TransEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var rate: Float,
    var time: Long,
    var dateTime: String,
    var credit: Boolean,
    var amount: Float//With sign always
)
