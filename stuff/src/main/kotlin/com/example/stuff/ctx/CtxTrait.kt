package com.example.stuff.ctx

import com.example.stuff.util.time.durationToNowInMillis
import mu.KLogger
import org.funktionale.tries.Try
import java.time.Instant
import java.util.*


data class Ctx<REQUEST>(val logId: UUID, val req: REQUEST, val logger: KLogger, val startedAt: Instant)

fun <REQUEST> Ctx<REQUEST>.ctxed(): Ctxed<REQUEST, REQUEST> = Ctxed(ctx = this, value = this.req)

data class Ctxed<REQUEST, T>(val ctx: Ctx<REQUEST>, val value: T)

fun <REQUEST, T, R> Ctxed<REQUEST, T>.andThen(block: (T) -> R): Ctxed<REQUEST, R> {
    return Ctxed(ctx = this.ctx, value = block(this.value))
}

fun <REQUEST, T, R> Ctxed<REQUEST, T>.andThenCtxed(block: (T, Ctx<REQUEST>) -> R): Ctxed<REQUEST, R> {
    return Ctxed(ctx = this.ctx, value = block(this.value, this.ctx))
}

fun <REQUEST, T> Ctxed<REQUEST, T>.andAlso(block: (T) -> Unit): Ctxed<REQUEST, T> {
    block(this.value)
    return this
}

interface ICtxHandler<REQUEST> {

    fun ctx(req: REQUEST, logger: KLogger, startedAt: Instant = Instant.now(), logId: UUID = UUID.randomUUID()) = Ctx(logId = logId, req = req, logger = logger, startedAt = startedAt)
    fun ctxed(
            req: REQUEST, logger: KLogger, startedAt: Instant = Instant.now(), logId: UUID = UUID.randomUUID()
    ): Ctxed<REQUEST, REQUEST> = ctx(req = req, logger = logger, startedAt = startedAt, logId = logId)
            .ctxed()

    fun <RESPONSE> Ctx<REQUEST>.execute(block: (Ctx<REQUEST>) -> RESPONSE): Try<RESPONSE> {
        return Try { block(this) }
                .onFailure {
                    logger.error {
                        "Handler Failed! " +
                                " reason: ${it.message}" +
                                " duration: ${startedAt.durationToNowInMillis()} ms" +
                                " logId: $logId" +
                                " req: $req"
                    }
                }
                .onSuccess {
                    logger.info {
                        "Handler Success." +
                                " duration: ${startedAt.durationToNowInMillis()} ms" +
                                " logId: $logId" +
                                " req: $req"
                    }
                }
    }

    fun <RESPONSE> Ctx<REQUEST>.handle(block: (REQUEST) -> RESPONSE): Try<RESPONSE> {
        return Try { block(this.req) }
                .onFailure {
                    logger.error {
                        "Handler Failed! " +
                                " reason: ${it.message}" +
                                " duration: ${startedAt.durationToNowInMillis()} ms" +
                                " logId: $logId" +
                                " req: $req"
                    }
                }
                .onSuccess {
                    logger.info {
                        "Handler Success." +
                                " duration: ${startedAt.durationToNowInMillis()} ms" +
                                " logId: $logId" +
                                " req: $req"
                    }
                }
    }
}
