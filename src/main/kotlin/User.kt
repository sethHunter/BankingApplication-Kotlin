package main.kotlin

open class User(val firstName:String, val lastName:String, val username:String, val password:String)
{
    open fun menu(): Boolean{return false}
}