package main.kotlin

import java.util.*

data class Transaction (val fromUser:String,
                        val fromAccount:String,
                        val toUser:String,
                        val toAccount:String,
                        val amount:Double,
                        val balance:Double,
                        val date: Date)