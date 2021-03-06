package pl.polsl.main

import java.util.*

fun Double.asPercent(decimals: Int = 2): String = (this * 100.0).format(decimals) + "%"

fun Double.format(decimals: Int = 2): String = "%.${decimals}f".format(this).replace(",", ".")

fun<T> LinkedList<T>.mRemove(numberOfElements: Int): List<T> {
    val list = LinkedList<T>()
    repeat(numberOfElements) {
        list.add(remove())
    }
    return list
}

fun waitTill(condition: () -> Boolean): Long {
    val delay = 10L
    var waitTime = 0L
    while (!condition()){
        waitTime += delay
        Thread.sleep(delay)
    }
    return waitTime
}