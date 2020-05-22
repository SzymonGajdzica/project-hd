package pl.polsl.strategy

class SampleLoadStrategy : LoadStrategy() {

    private var pagesToLoad = 1

    override fun getNumberOfPages(remainingElements: Int): Int {
        return if (remainingElements < pageSize * pagesToLoad) pagesToLoad else 0
    }

    override fun getInitialNumberOfPages(): Int {
        return pagesToLoad * 2
    }

    override fun analyzeData(waitTime: Long, bufferSize: Int, loadedPages: Int) {
        super.analyzeData(waitTime, bufferSize, loadedPages)
        if(waitTime != 0L)
            pagesToLoad += 1
    }

}