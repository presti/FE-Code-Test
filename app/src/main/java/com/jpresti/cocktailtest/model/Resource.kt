package com.jpresti.cocktailtest.model

/**
 * A generic class that contains data and status about loading this data.
 */
class Resource<out T> private constructor(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS, ERROR
    }

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String?, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }
    }

    fun isError() = status == Status.ERROR
}
