package pl.polsl.main

fun waitTill(condition: () -> Boolean): Long {
    val delay = 10L
    var waitTime = 0L
    while (!condition()){
        waitTime += delay
        Thread.sleep(delay)
    }
    return waitTime
}