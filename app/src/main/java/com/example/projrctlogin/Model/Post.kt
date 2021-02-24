package com.example.projrctlogin.Model

class Post {
    private var postid: String = ""
    private var postimage: String = ""
    private var publisher: String = ""
    private var discription: String = ""

    constructor()
    {

    }

    constructor(postid: String, postimage: String, publisher: String, discription: String) {
        this.postid = postid
        this.postimage = postimage
        this.publisher = publisher
        this.discription = discription
    }


    fun getpostid(): String
    {
        return postid
    }

    fun getpostimage(): String
    {
        return postimage
    }

    fun getpublisher(): String
    {
        return publisher
    }

    fun getdiscription(): String
    {
        return discription
    }



    fun setpostid(postid: String)
    {
        this.postid = postid
    }

    fun setpostimage(postimage: String)
    {
        this.postimage = postimage
    }

    fun setpostdiscription(discription: String)
    {
        this.discription = discription
    }

    fun setpostpublisher(publisher: String)
    {
        this.publisher = publisher
    }

}
