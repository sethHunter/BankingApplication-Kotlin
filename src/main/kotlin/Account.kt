package main.kotlin

import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class Account (val name:String, private var balance:Double)
{
    private val transactions = ArrayList<Transaction>()

    fun displayTransactions()
    {
        Clear.clear()
        println("How would you like to sort the transactions?\n")
        println("1. Receiving user")
        println("2. Date")
        println("3. Amount")
        println("\nPlease enter the number associated with what you want to do and press enter")

        val scanner = Scanner(System.`in`)
        val choice = scanner.nextLine()
        var choiceN = 0

        try {
            choiceN = choice.toInt()
        } catch(e: Exception){}

        when(choiceN)
        {
            1->transactions.sortBy { it.toUser }
            2->transactions.sortBy { it.date }
            3->transactions.sortBy { it.amount }
            else->
            {
                println("\nIncorrect input \nPress enter to try again")
                readLine()
            }
        }



        if (transactions.isNotEmpty())
        {

            println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
            for(transaction in transactions)
                println("${transaction.fromUser}'s ${transaction.fromAccount}   to    ${transaction.toUser}'s ${transaction.toAccount}  on ${transaction.date}    |    Amount = ${transaction.amount}    |    Balance = ${transaction.balance}   |")
            println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
            println("\n\nPress enter to go back to the menu")
            readLine()
        } else
        {
            println("There are no transaction to show")
            println("Press enter to go back to the menu")
            readLine()

        }
    }

    override fun toString(): String {
        println("\nYou balance for $name is $balance")
        println("Press enter to go back to the menu\n")
        readLine()

        return ""
    }

    fun acceptTransaction(fromUser:String, fromAccount:String, toUser:String, toAccount:String, amount:Double, sender:Boolean)
    {
        if (sender)
        {
            balance-=amount
            val transaction = Transaction(fromUser, fromAccount, toUser, toAccount, -(amount), balance, Date())
            Database.logTransaction(transaction)
            transactions.add(transaction)
        } else
        {
            balance+=amount
            val transaction = Transaction(fromUser, fromAccount, toUser, toAccount, amount, balance, Date())
            Database.logTransaction(transaction)
            transactions.add(transaction)
        }

    }

    fun giveBalance(): Double
    {
        return balance
    }

    fun deposit(amount: Double, username: String)
    {
        balance += amount
        val transaction = Transaction(username, name, username, name, amount, balance, Date())
        Database.logTransaction(transaction)
        transactions.add(transaction)

        println("\n\nYour deposit of $amount was successful your new balance is $balance \n Press enter to continue")
        readLine()
    }

    fun withdraw(amount: Double, username: String)
    {
        if(amount > balance)
        {
            balance -= amount
            val transaction = Transaction(username, name, username, name, -(amount), balance, Date())
            Database.logTransaction(transaction)
            transactions.add(transaction)

            println("\n\nYour withdraw of $amount was successful your new balance is $balance \n Press enter to continue")
            readLine()
        } else
            throw Exception()
    }
}