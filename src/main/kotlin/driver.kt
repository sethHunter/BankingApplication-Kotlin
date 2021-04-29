package main.kotlin

import java.lang.Exception
import java.util.*


fun main(args: Array<String>)
{
    val scanner = Scanner(System.`in`)
    var stay = true
    var attempts = 3

    do {

        Clear.clear()
        println(".-.  .-. .----. .-.    .----.  .---.  .-.  .-. .----. \n" +
                "| {  } | } |__} } |    | }`-' / {-. \\ }  \\/  { } |__} \n" +
                "{  /\\  } } '__} } '--. | },-. \\ '-} / | {  } | } '__} \n" +
                "`-'  `-' `----' `----' `----'  `---'  `-'  `-' `----' \n\n")
        println("1. Log in")
        println("2. Create an account")
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
                do{
                    println("\nWhat is your username. \nPress enter if you have made a mistake and would like to go back to the main menu")
                    val username = scanner.nextLine()

                    if (username == "")
                        break

                    println("\nWhat is your password")
                    val password = scanner.nextLine()

                    if(Database.logIn(username, password))
                    {
                        stay = Database.start()
                        break;
                    }
                    else
                    {
                        Clear.clear()
                        println("\nError log in information incorrect")
                        attempts--
                        println("You now have $attempts attempts left")
                    }
                } while(attempts > 0)

                if(attempts == 0)
                    stay = false
            }
            2->
            {
                Clear.clear()

                println("\nWhat do you want your first name?")
                val firstName = scanner.nextLine()

                println("\nWhat do you want your last name?")
                val lastName = scanner.nextLine()

                var unique  = false

                var username = ""

                do {
                    println("\nWhat would you like your username to be?")
                    username = scanner.nextLine()

                    if (!Database.hasUser(username))
                        unique  = true
                    else
                        println("\nSorry someone already has that username please choose another one")
                } while (!unique )

                println("\nWhat would you like your password to be?")
                val password = scanner.nextLine()

                println("\nAre you an employee? \nType Yes if you are. If you are not just press enter")
                val temp = scanner.nextLine()
                var employee = false

                if (temp.toUpperCase() == "YES")
                    employee = true

                Database.add(firstName, lastName, username, password, employee)
                Database.logIn(username, password)
                Database.start()
            }
            else->
            {
                println("\nIncorrect input \nPress enter to try again")
                readLine()
            }
        }
    } while(stay)

    Database.save()
}