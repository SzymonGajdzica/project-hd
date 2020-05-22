package pl.polsl.strategy

import pl.polsl.main.Main
import pl.polsl.main.format
import java.util.*

abstract class LoadStrategy {

    protected val maxBufferSize = Main.maxBufferSize
    protected val pageSize = Main.pageSize
    protected val waitTimes = LinkedList<Long>()
    protected val bufferSizes = LinkedList<Int>()
    protected val loadedPagesList = LinkedList<Int>()

    abstract fun getNumberOfPages(remainingElements: Int): Int

    abstract fun getInitialNumberOfPages(): Int

    open fun analyzeData(waitTime: Long, bufferSize: Int, loadedPages: Int) {
        bufferSizes.add(bufferSize)
        waitTimes.add(waitTime)
        loadedPagesList.add(loadedPages)
    }

    open fun analyzeInitialData(waitTime: Long, loadedPages: Int) {

    }

    open fun generateReport(): String {
        return buildString {
            append("| <Name> | <max value> | <min value> | <average value> | <sum value> |\n")
            append("Wait time | ${waitTimes.max()} | - | ${waitTimes.average().format()} | ${waitTimes.sum()} |\n")
            append("Buffer size | ${bufferSizes.max()} | ${bufferSizes.min()} | ${bufferSizes.average().format()} | - |\n")
            append("Loaded pages | ${loadedPagesList.max()} | - | - | ${loadedPagesList.sum()} |\n")
        }
    }

}
