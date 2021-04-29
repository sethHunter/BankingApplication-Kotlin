package main.kotlin

import java.io.File

object BankLogger
{
    fun log(text: String)
    {
        File("log.txt").appendText(text)
    }
}