package com.example.projrctlogin.Model

class User {
    private var fname : String = ""
    private var lname : String = ""
    private var email : String = ""
    private var typeofaccount : String = ""
    private var stream : String = ""
    private var image : String = ""
    private var dateofbirth : String = ""
    private var gender : String = ""
    private var mobileno : String = ""
    private var password : String = ""
    private var uid : String = ""


    constructor()

    constructor(fname : String,lname : String,email : String,typeofaccount : String,stream : String,image : String)
    {
        this.fname=fname
        this.lname=lname
        this.email=email
        this.typeofaccount=typeofaccount
        this.stream=stream
        this.image=image
    }

    fun getUsername () : String
    {
        return this.fname
    }

    fun setUsername (fname : String)
    {
        this.fname=fname
    }



    fun getLastname () : String
    {
        return lname
    }

    fun setLastname (lname : String)
    {
        this.lname=lname
    }



    fun getEmailame () : String
    {
        return email
    }

    fun setEmailname (email : String)
    {
        this.email=email
    }



    fun getToaname () : String
    {
        return typeofaccount
    }

    fun setToaname (typeofaccount : String)
    {
        this.typeofaccount=typeofaccount
    }



    fun getStreamname () : String
    {
        return stream
    }

    fun setStreamname (stream : String)
    {
        this.stream=stream
    }


    fun getImage () : String
    {
        return image
    }

    fun setImage (image : String)
    {
        this.image=image
    }

}