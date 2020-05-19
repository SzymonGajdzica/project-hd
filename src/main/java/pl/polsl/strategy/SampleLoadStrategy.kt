package pl.polsl.strategy

class SampleLoadStrategy : LoadStrategy() {

    private var pagesToLoad = 1

    override fun getNumberOfPages(remainingElements: Int): Int {
        return if (remainingElements < pageSize * 2) pagesToLoad else 0
    }

    override fun getInitialNumberOfPages(): Int {
        return 5
    }

    override fun analyzeData(waitTime: Long, bufferSize: Int) {
        super.analyzeData(waitTime, bufferSize)
        if(waitTime != 0L)
            pagesToLoad += 5
    }

}