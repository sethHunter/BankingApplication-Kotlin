package main.kotlin

import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json

object Database
{
    private val employees = ArrayList<Employee>()
    private val customers = ArrayList<Customer>()
    private val transactions = ArrayList<Transaction>()
    private var userIndex = -1
    private var userIsEmployee = false

    init {
        load()
    }

    /*
    Starts the application after you log in
    Calls the menu of the user
     */
    fun start(): Boolean
    {
        //Start application with user that is logged in
        //User Index being set to -1 means no one has logged in
        if (userIndex != -1)
        {
            return if (userIsEmployee) {
                BankLogger.log("User ${employees[userIndex].username} has logged in at ${Date()}")
                val result = employees[userIndex].menu()
                userIndex = -1
                userIsEmployee = false
                result
            } else {
                return if (customers[userIndex].approved)
                {
                    BankLogger.log("User ${customers[userIndex].username} has logged in at ${Date()}")
                    val result = customers[userIndex].menu()
                    userIndex = -1
                    result
                } else
                {
                    println("\n\nYou are not approved yet \n Press enter to go back to the main menu")
                    readLine()
                    true
                }
            }
        }

        userIndex = -1
        return false
    }

    /*
    Log in function that sets the userIndex to the index connected of said user with the username and password provided
     */
    fun logIn(username:String, password:String): Boolean
    {
        for ((i, employee) in employees.withIndex())
        {
            if(employee.username == username)
                if(employee.password == password)
                {
                    userIndex = i
                    userIsEmployee = true
                    return true
                }
        }

        for ((i, customer) in customers.withIndex())
        {
            if(customer.username == username)
                if(customer.password == password)
                {
                    userIndex = i
                    userIsEmployee = false
                    return true
                }
        }
        return false
    }

    /*
    Function is used to send the transaction to the sender and receiver users
     */
    fun startTransfer(fromUser:String, fromAccount:String, toUser:String, amount:Double)
    {
        println("\nSelect the account you would like to deposit into for $toUser")
        val toAccount = customers[findUserIndex(toUser)].selectAccountForTransfer()

        customers[findUserIndex(fromUser)].addTransaction(fromUser, fromAccount, toUser, toAccount, amount, true)
        customers[findUserIndex(toUser)].addTransaction(fromUser, fromAccount, toUser, toAccount, amount, false)
    }

    /*
    Function is used to log withdraw and deposit transactions
     */
    fun logTransaction(transaction: Transaction)
    {
        transactions.add(transaction)
    }

    fun hasUser(username: String): Boolean
    {
        for ((i, employee) in employees.withIndex())
        {
            if(employee.username == username)
                return true
        }

        for ((i, customer) in customers.withIndex())
        {
            if(customer.username == username)
                return true
        }

        return false
    }

    /*
    Simple view all transactions function to be used by the employee
     */
    fun viewAll()
    {
        Clear.clear()
        println("How would you like to sort the transactions?\n")

        println("1. Sending user ")
        println("2. Receiving user")
        println("3. Date")
        println("4. Amount")
        println("\nPlease enter the number associated with what you want to do and press enter")

        val scanner = Scanner(System.`in`)
        val choice = scanner.nextLine()
        var choiceN = 0

        try {
            choiceN = choice.toInt()
        } catch(e: Exception){}

        when(choiceN)
        {
            1->transactions.sortBy { it.fromUser }
            2->transactions.sortBy { it.toUser }
            3->transactions.sortBy { it.date }
            4->transactions.sortBy { it.amount }
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
            println("\n\nThere are no transaction to show")
            println("Press enter to go back to the menu")
            readLine()
        }
    }

    /*
    Function to be used by the transfer function that allows the user to select the account they want to deposit into
     */
    private fun selectUserAccount(username: String): String
    {
        //Selects a user account giving the number associated with it and returns
        return ""
    }

    /*
    Adds a user to the database
     */
    fun add(firstName: String, lastName: String, username: String, password: String, employee: Boolean) {
        if (employee)
            employees.add(Employee(firstName, lastName, username, password))
        else
            customers.add(Customer(firstName, lastName, username, password))
        logIn(username, password)
    }

    /*
    Returns a user's index in the array given the username
     */
    private fun findUserIndex(username: String): Int
    {
        for ((i, employee) in employees.withIndex())
        {
            if(employee.username == username)
                return i
        }

        for ((i, customer) in customers.withIndex())
        {
            if(customer.username == username)
                return i
        }
        return -1
    }

    /*
    User index being set to -1 means that no one is logged in
    This function can only be used by an employee on themselves
     */
    fun deleteEmployee()
    {
        if (userIndex != -1) employees.removeAt(userIndex)
            userIndex = -1
    }

    /*
    Delete a customer from the database
     */
    fun deleteCustomer(username: String)
    {
        val userToDelete = findUserIndex(username)
        if (userToDelete != -1) customers.removeAt(userToDelete)
    }

    /*
    Used by an employee to allows a customer to start using their account
     */
    fun approveCustomer(username: String)
    {
        val userToApprove = findUserIndex(username)
        if (userToApprove != -1) customers[userToApprove].approved = true
    }

    private fun load()
    {

    }

    fun save()
    {

    }
}