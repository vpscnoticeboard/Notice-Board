package com.example.projrctlogin.Model

class Comment
{
    private var comment: String = ""
    private var publisher: String = ""


    constructor()
    {

    }

    constructor(comment: String, publisher: String) {
        this.comment = comment
        this.publisher = publisher
    }

    fun getcomment(): String
    {
        return comment
    }

    fun getpublisher(): String
    {
        return publisher
    }


    fun setcomment(comment: String)
    {
        this.comment = comment
    }

    fun setpublisher(publisher: String)
    {
        this.publisher = publisher
    }
}