package com.example.projrctlogin.Model

class User {
    private var fname : String = ""
    private var lname : String = ""
    private var stream : String = ""
    private var typeofaccount : String = ""
    private var email : String = ""
    private var image : String = ""
    private var uid : String = ""
    private var dateofbirth : String =""

    constructor(){

    }

    constructor(fname: String, lname: String, stream: String, typeofaccount: String, email: String, image: String, uid: String, dateofbirth: String)
    {
        this.fname = fname
        this.lname = lname
        this.stream = stream
        this.typeofaccount = typeofaccount
        this.email = email
        this.image = image
        this.uid = uid
        this.dateofbirth = dateofbirth
    }

    fun getFname(): String
    {
        return fname
    }
    fun setFname(fname: String)
    {
        this.fname = fname
    }

    fun getLname(): String
    {
        return lname
    }
    fun setLname(lname: String)
    {
        this.lname = lname
    }


    fun getStream(): String
    {
        return stream
    }
    fun setStream(stream: String)
    {
        this.stream = stream
    }


    fun getTypeofaccount(): String
    {
        return typeofaccount
    }
    fun setTypeofaccount(typeofaccount: String)
    {
        this.typeofaccount = typeofaccount
    }


    fun getEmail(): String
    {
        return email
    }
    fun setEmail(email: String)
    {
        this.email = email
    }


    fun getImage(): String
    {
        return fname
    }
    fun setImage(image: String)
    {
        this.image = image
    }


    fun getUid(): String
    {
        return uid
    }
    fun setUid(uid: String)
    {
        this.uid = uid
    }



    fun getDob(): String
    {
        return dateofbirth
    }
    fun setDob(dateofbirth: String)
    {
        this.dateofbirth = dateofbirth
    }


}