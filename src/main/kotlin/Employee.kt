package main.kotlin

import java.lang.Exception
import java.util.*

class Employee(firstName:String, lastName:String, username:String, password:String): User(firstName, lastName, username, password)
{
    override fun menu(): Boolean
    {
        Clear.clear()
        val scanner = Scanner(System.`in`)
        println("\nHello $firstName!\n")
        println("1. View all transactions")
        println("2. Approve a user")
        println("3. Delete a user")
        println("4. Delete yourself")
        println("5. Log out")
        println("6. Close application")
        println("\nPlease enter the number associated with what you want to do and press enter")

        val choice = scanner.nextLine()
        var choiceN = 0

        try {
            choiceN = choice.toInt()
        } catch(e: Exception){}

        when(choiceN)
        {
            1-> Database.viewAll()
            2->
            {
                Clear.clear()
                println("What is the username of the customer you want to approve")
                val customerUsername = scanner.nextLine()
                Database.approveCustomer(customerUsername)
            }
            3->
            {
                Clear.clear()
                println("What is the username of the employee you want to delete")
                val customerUsername = scanner.nextLine()
                Database.deleteCustomer(customerUsername)
            }
            4->
            {
                Database.deleteEmployee()
                return true
            }
            5-> return true
            6-> return false
            else->
            {
                println("\nIncorrect input \nPress enter to try again")
                readLine()
                return menu()
            }
        }

        return menu()
    }
}