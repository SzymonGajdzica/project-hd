package pl.polsl.main

import pl.polsl.data.manager.Connector
import pl.polsl.data.source.DataSource
import pl.polsl.data.source.DataSourceInfo
import pl.polsl.strategy.LoadStrategy
import pl.polsl.user.UserSimulator
import pl.polsl.user.UserSimulatorInfo

object Main {

    var maxBufferSize = 500
        private set
    var pageSize = 10
        private set

    @JvmStatic
    fun main(args: Array<String>) {
        val dataSourceInfo = DataSourceInfo(
                numberOfProducers = 20,
                produceTimeRange = 1L..2L,
                connectionTimeRange = 200L..400L,
                singlePageLoadRange = 10L..300L
        )
        val userSourceInfo = UserSimulatorInfo(
                fetchedElementsByUserRange = 4000..5000,
                maxNumberOfUsers = 1000,
                userSpawnTimeRange = 1L..2L,
                userStaticAskDelayRange = 1L..10L,
                userDynamicAskDelayRange = 2L..50L
        )
        val dataSource = DataSource(dataSourceInfo)
        val loadStrategyType = LoadStrategy.Type.ADAPTIVE_RENEW
        val connector = Connector(dataSource, loadStrategyType)
        val userSimulator = UserSimulator(connector, userSourceInfo)
        Logger.initialize(userSourceInfo, dataSourceInfo, LoadStrategy.getLoadStrategy(loadStrategyType).javaClass.simpleName)
        userSimulator.startSimulation()
    }

}