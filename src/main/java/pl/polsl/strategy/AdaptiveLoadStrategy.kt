package pl.polsl.strategy

class AdaptiveLoadStrategy : LoadStrategy() {

    private var pagesToLoad = 1

    override fun getNumberOfPages(remainingElements: Int): Int {
        return if (remainingElements < pageSize * pagesToLoad) pagesToLoad else 0
    }

    override fun getInitialNumberOfPages(): Int {
        return pagesToLoad * 2
    }

    override fun analyzeData(loadData: LoadData) {
        super.analyzeData(loadData)
        if(loadData.waitTime != 0L)
            pagesToLoad += 1
        //TODO improve algorithm
    }

}