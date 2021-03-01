package com.example.projrctlogin.Model

class Notification {
    private var userid: String = ""
    private var text: String = ""
    private var postid: String = ""
    private var ispost = false

    constructor()
    {

    }

    constructor(userid: String, text: String, postid: String, ispost: Boolean) {
        this.userid = userid
        this.text = text
        this.postid = postid
        this.ispost = ispost
    }

    fun getUserId(): String
    {
        return userid
    }

    fun getText(): String
    {
        return text
    }

    fun getPostId(): String
    {
        return postid
    }

    fun getIspost(): Boolean
    {
        return ispost
    }


    fun setTextId(userid: String)
    {
        this.userid = userid
    }

    fun setUserId(text: String)
    {
        this.text = text
    }

    fun setPostId(postid: String)
    {
        this.postid = postid
    }

    fun setIspost(ispost: Boolean)
    {
        this.ispost = ispost
    }
}