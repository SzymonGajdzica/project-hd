package pl.polsl.main

import pl.polsl.data.manager.Connector
import pl.polsl.data.source.DataSource
import pl.polsl.data.source.DataSourceInfo
import pl.polsl.strategy.LoadStrategy
import pl.polsl.user.UserSimulator
import pl.polsl.user.UserSimulatorInfo

object Main {

    var maxBufferSize = 200
        private set
    var pageSize = 10
        private set

    @JvmStatic
    fun main(args: Array<String>) {
        val dataSourceInfo = DataSourceInfo(
                numberOfProducers = 20,
                produceTimeRange = 1L..2L,
                connectionTimeRange = 100L..125L,
                singlePageLoadRange = 5L..10L
        )
        val userSourceInfo = UserSimulatorInfo(
                fetchedElementsByUserRange = 2000..3000,
                maxNumberOfUsers = 1000,
                userSpawnTimeRange = 1L..2L,
                userStaticAskDelayRange = 2L..4L,
                userDynamicAskDelayRange = 1L..20L
        )
        val dataSource = DataSource(dataSourceInfo)
        val loadStrategyType = LoadStrategy.Type.ADAPTIVE_RENEW
        val connector = Connector(dataSource, loadStrategyType)
        val userSimulator = UserSimulator(connector, userSourceInfo)
        Logger.initialize(userSourceInfo, dataSourceInfo, LoadStrategy.getLoadStrategy(loadStrategyType).javaClass.simpleName)
        userSimulator.startSimulation()
    }

}