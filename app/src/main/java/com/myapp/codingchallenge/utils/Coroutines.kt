package com.myapp.codingchallenge.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * I choose Coroutines in handling suspendable and long process executing functions so that it can provide a seamless and non-blocking UI experience for the users.
 * This gives advantage in applications that are handling big records in a single page.
 */
object Coroutines {

    /** This is a coroutine scope function that is taking a suspendable function as 'work' and executing it in the main thread. */
    fun main(work: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }

    /**
     * This is a coroutine scope function that is taking a suspendable function as 'work' also and
     * executing it in the io thread, good for long process functions and no ui views must be updated.
     */
//    fun io(work: suspend (() -> Unit)) =
//        CoroutineScope(Dispatchers.IO).launch {
//            work()
//        }
}