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
                produceTimeRange = 2L..4L,
                connectionTimeRange = 1000L..2000L,
                singleDataLoadRange = 1L..2L
        )
        val userSourceInfo = UserSimulatorInfo(
                fetchedElementsByUserRange = 1000..2000,
                maxNumberOfUsers = 1000,
                userSpawnTimeRange = 1L..10L,
                userStaticAskDelayRange = 5L..30L,
                userDynamicAskDelayRange = 5L..50L
        )
        val dataSource = DataSource(dataSourceInfo)
        val loadStrategyType = LoadStrategy.Type.ADAPTIVE
        val connector = Connector(dataSource, loadStrategyType)
        val userSimulator = UserSimulator(connector, userSourceInfo)
        Logger.initialize(userSourceInfo, dataSourceInfo, LoadStrategy.getLoadStrategy(loadStrategyType).javaClass.simpleName)
        userSimulator.startSimulation()
    }

}