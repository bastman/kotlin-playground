package com.example.stuff.handler

import com.example.stuff.ctx.*
import mu.KLogging
import java.time.Instant

data class MyRequest(val foo: String)
data class MyResponse(val bar: String)

private typealias Request = MyRequest
private typealias Response = MyResponse
private typealias Context = Ctx<Request>

class MyHandler() {
    companion object : KLogging(), ICtxHandler<Request>

    fun handle(req: Request): Response = ctx(req, logger)
            .execute(::pipeline)
            .get()

    private fun pipeline(ctx: Context): Response = ctx.ctxed()
            .andThen { Instant.now() }
            .andThen(::step2)
            .andThen(::step3)
            .andAlso { logger.info { it } }
            .andThenCtxed(::step4)
            .let { Response(bar = "baz: ${it.ctx.req.foo}- ${it.value}") }

    private fun step2(source: Instant): Long {
        return source.epochSecond
    }

    private fun step3(source: Long): String {
        return source.toString()
    }

    private fun step4(source: String, ctx: Context): String {
        return "${source.toUpperCase()} - ${ctx.logId}"
    }
}
