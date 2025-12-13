package com.macuguita.petal_smp.common.tpa

import java.util.*

object TpaManager {
    private const val EXPIRY_MS = 60_000L

    private val requests = mutableMapOf<UUID, MutableList<TpaRequest>>()

    fun sendRequest(request: TpaRequest): Boolean {
        val list = requests.getOrPut(request.target) { mutableListOf() }

        if (list.any { it.requester == request.requester && !isExpired(it) }) {
            return false
        }

        list.add(request)
        return true
    }

    fun getRequests(target: UUID): List<TpaRequest> {
        cleanup(target)
        return requests[target].orEmpty()
    }

    fun popMostRecent(target: UUID): TpaRequest? {
        cleanup(target)
        return requests[target]?.maxByOrNull { it.timestamp }
            ?.also { requests[target]?.remove(it) }
    }

    fun popFromRequester(target: UUID, requester: UUID): TpaRequest? {
        cleanup(target)
        val list = requests[target] ?: return null
        val req = list.find { it.requester == requester }
        if (req != null) list.remove(req)
        return req
    }

    private fun cleanup(target: UUID) {
        requests[target]?.removeIf { isExpired(it) }
        if (requests[target]?.isEmpty() == true) {
            requests.remove(target)
        }
    }

    private fun isExpired(req: TpaRequest): Boolean {
        return System.currentTimeMillis() - req.timestamp > EXPIRY_MS
    }
}
