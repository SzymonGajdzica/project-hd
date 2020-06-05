package pl.polsl.strategy

import kotlin.math.min
import kotlin.math.roundToInt

class AdaptiveRenewLoadStrategy: LoadStrategy() {

    private enum class State {
        COLLECTING_STATS, WORKING, FULL_RENEW
    }

    private var state = State.COLLECTING_STATS
    private var pagesToLoad = 1

    private var cyclesWithoutChangeCounter = 0
    private var lastLoadedPages = 0
    private var lowestSize = maxBufferSize

    private val maxCyclesWithoutChange: Int
        get() = ((maxBufferSize / pageSize).toDouble() / pagesToLoad.toDouble()).roundToInt()
    private val loadMargin: Int
        get() = (pagesToLoad * pageSize * 0.1).roundToInt()
    private val standardLoadBorder: Int
        get() = maxBufferSize - (pageSize * pagesToLoad)
    private val adaptedLoadBorder: Int by lazy {
        min(maxBufferSize - lowestSize + loadMargin, maxBufferSize - (pageSize * pagesToLoad))
    }

    override fun getNumberOfPages(remainingElements: Int): Int {
        return when (state) {
            State.COLLECTING_STATS -> if (remainingElements <= standardLoadBorder) pagesToLoad else 0
            State.WORKING -> if (remainingElements <= adaptedLoadBorder) pagesToLoad else 0
            State.FULL_RENEW -> if(remainingElements <= maxBufferSize / pageSize / 2) maxBufferSize / pageSize / 2 else 0
        }
    }

    override fun getInitialNumberOfPages(): Int {
        return maxBufferSize / pageSize
    }

    override val logMessage: String?
        get() = "adaptedLoadBorder=$adaptedLoadBorder, pagesToLoad=$pagesToLoad, state=$state"

    override fun analyzeData(loadData: LoadData) {
        super.analyzeData(loadData)
        if (state == State.COLLECTING_STATS) {
            lowestSize = min(loadData.initialBufferSize, lowestSize)
            if (lowestSize < maxBufferSize - (2 * pagesToLoad * pageSize)) {
                pagesToLoad++
                if (pagesToLoad >= maxBufferSize / pageSize / 2) {
                    state = State.FULL_RENEW
                } else {
                    lowestSize = maxBufferSize
                    lastLoadedPages = 0
                    cyclesWithoutChangeCounter = 0
                }
            }
            if (loadData.loadedPages != 0) {
                if (lastLoadedPages == loadData.loadedPages) {
                    cyclesWithoutChangeCounter++
                } else {
                    lastLoadedPages = loadData.loadedPages
                    cyclesWithoutChangeCounter = 0
                }
            }
            if (cyclesWithoutChangeCounter >= maxCyclesWithoutChange) {
                state = State.WORKING
            }
        } else if(state == State.WORKING) {
            if(loadData.waitTime != 0L){
                state = State.COLLECTING_STATS
                lastLoadedPages = 0
                cyclesWithoutChangeCounter = 0
            }
        }
    }

}