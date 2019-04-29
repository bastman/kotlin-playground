package com.example.stuff

import com.example.stuff.handler.MyHandler
import com.example.stuff.handler.MyRequest
import com.example.stuff.util.time.durationToNowInMillis
import java.time.Instant
import java.util.*


fun main(args: Array<String>) {
    println("main")

    val startedAt = Instant.now()
    val cnt = 100
    (0..cnt).forEach {
        val req = MyRequest(foo = "FOOOOO-${UUID.randomUUID()}")
        val resp = handler.handle(req)
        println("req: $req - resp: $resp")
    }

    val ms = startedAt.durationToNowInMillis()
    println(("=== DONE: $ms ms --- avg: ${ms / cnt}"))


}

private val handler = MyHandler()

