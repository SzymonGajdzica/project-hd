package pl.polsl.main

import java.util.*

fun Double.format(precision: Int = 2): String {
    return String.format("%1$,.${precision}f", this).replace(",", ".")
}

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