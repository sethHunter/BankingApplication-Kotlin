package main.kotlin

import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class Customer(firstName:String, lastName:String, username:String, password:String): User(firstName, lastName, username, password)
{
    private val accounts = ArrayList<Account>()
    var approved = false

    override fun menu(): Boolean
    {
        Clear.clear()
        val scanner = Scanner(System.`in`)
        println("\nHello $firstName!\n")
        println("1. View account balance")
        println("2. View account transactions")
        println("3. Make a transfer")
        println("4. Make deposit")
        println("5. Make withdraw")
        println("6. Make an account")
        println("7. Log out")
        println("8. Close application")
        println("\nPlease enter the number associated with what you want to do and press enter")

        val choice = scanner.nextLine()
        var choiceN = 0

        try {
            choiceN = choice.toInt()
        } catch(e: Exception){}

        when(choiceN)
        {
            1->
            {
                Clear.clear()
                if (hasAccounts())
                {
                    try {
                        accounts[selectAccount()].toString()
                    } catch (e: Exception)
                    {
                        println("\n\nInvalid input ")
                        println("Press enter to return to the menu")
                        readLine()
                    }
                }
                else
                {
                    println("You have no accounts")
                    println("Press enter to return to the menu")
                    readLine()
                }

            }
            2->
            {
                Clear.clear()
                if (hasAccounts())
                {
                    try {
                        accounts[selectAccount()].displayTransactions()
                    } catch (e: Exception)
                    {
                        println("\n\nInvalid input ")
                        println("Press enter to return to the menu")
                        readLine()
                    }
                }
                else
                {
                    println("You have no accounts")
                    println("Press enter to return to the menu")
                    readLine()
                }
            }
            3->
            {
                Clear.clear()
                if (hasAccounts())
                    try {
                        println()
                        val userAccount = selectAccountForTransfer()

                        println("\nPlease input the username of the person you would like to transfer to")
                        val toUser = scanner.nextLine()

                        println("\nPlease input the amount you would like to transfer")
                        val amount = (scanner.nextLine()).toDouble()

                        if (amount > getBalance(userAccount))
                        {
                            println("You do not have enough in your account currently to make this transfer")
                            throw Exception()
                        }
                        else if (amount < 0)
                        {
                            throw Exception()
                        } else
                            Database.startTransfer(username, userAccount, toUser, amount)
                    } catch (e: Exception)
                    {
                        println("\n\nInvalid input ")
                        println("Press enter to return to the menu")
                        readLine()
                    }
                else
                {
                    println("You have no accounts")
                    println("Press enter to return to the menu")
                    readLine()
                }
            }
            4->
            {
                Clear.clear()
                if (hasAccounts())
                {
                    println("Please input the amount you would like to deposit\n")
                    val temp = scanner.nextLine()
                    var amount = 0.0

                    try {
                        amount = temp.toDouble()

                        if (amount < 0)
                            throw Exception()

                        accounts[selectAccount()].deposit(amount, username)
                    } catch(e: Exception)
                    {
                        println("You entered an invalid amount\n Press enter to return to the main menu")
                        readLine()
                        menu()
                    }
                }
                else
                {
                    println("You have no accounts")
                    println("Press enter to return to the menu")
                    readLine()
                }
            }
            5->
            {
                Clear.clear()
                if (hasAccounts())
                {
                    println("Please input the amount you would like to withdraw\n")
                    val temp = scanner.nextLine()
                    var amount = 0.0

                    try {
                        amount = temp.toDouble()
                        accounts[selectAccount()].withdraw(amount, username)
                    } catch(e: Exception)
                    {
                        println("You entered an invalid amount\n Press enter to return to the main menu")
                        readLine()
                        menu() // error
                    }
                }
                else
                {
                    println("You have no accounts")
                    println("Press enter to return to the menu")
                    readLine()
                }
            }
            6-> {
                Clear.clear()
                println("What would you like to name your new account")
                val tempName = readLine()

                var amount = 0.0
                do {
                    println("\n\nThere is a minimum deposit of 5 dollars to open an account")
                    println("How much would you like to deposit?")
                    val temp = scanner.nextLine()

                    try {
                        amount = temp.toDouble()

                        if (amount < 5)
                            println("\n\nThe amount of $amount is not enough to open an account \n\n")
                    } catch(e: Exception)
                    {
                        println("You entered an invalid amount\n Press enter to return to the main menu")
                        readLine()
                        menu()
                    }
                } while (amount < 5)

                accounts.add(Account(tempName!!, amount))
            }
            7-> return true
            8-> return false
            else->
            {
                println("\nIncorrect input \nPress enter to try again")
                readLine()
                return menu()
            }
        }

        return menu()
    }

    private fun selectAccount(): Int
    {
        val scanner = Scanner(System.`in`)
        println("\nPlease select an account from the options below")
        toString()
        val choice = scanner.nextLine()
        var choiceN = 0

        try {
            choiceN = choice.toInt()
        } catch(e: Exception){}

        return --choiceN
    }

    private fun getBalance(name:String): Double
    {
        for (account in accounts)
            if (account.name == name)
            {
                return account.giveBalance()
            }

        return 0.0
    }

    /*
    Simple function to check to see if the customer has any accounts before trying something
     */
    private fun hasAccounts(): Boolean
    {
        if (accounts.isNotEmpty())
            return true

        return false
    }

    fun selectAccountForTransfer(): String {
        val scanner = Scanner(System.`in`)
        println("Please select an account from the options below")
        toString()

        return accounts[(scanner.nextLine().toInt())-1].name
    }

    fun addTransaction(fromUser:String, fromAccount:String, toUser:String, toAccount:String, amount:Double, sender:Boolean)
    {
        if (sender)
        {
            for ((i) in accounts.withIndex())
            {
                if (accounts[i].name == fromAccount)
                    accounts[i].acceptTransaction(fromUser, fromAccount, toUser, toAccount, amount, sender)
            }
        }
        else
        {
            for ((i) in accounts.withIndex())
            {
                if (accounts[i].name == toAccount)
                    accounts[i].acceptTransaction(fromUser, fromAccount, toUser, toAccount, amount, sender)
            }
        }
    }

    override fun toString(): String
    {
        for ((i, account) in accounts.withIndex())
        {
            println("${i+1}. ${account.name}")
        }
        return super.toString()
    }
}