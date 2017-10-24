package com.github.mideo.apitestkit.gatling

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object GatlingTestRunner {
    private lazy val props = new GatlingPropertiesBuilder

    def forSimulationClass(s: String): this.type = {
      props.simulationClass(s)
      this
    }

    def saveResultDirectoryTo(s: String): this.type = {
      props.resultsDirectory(s)
      this
    }

    def fromSourceDirectory(s: String): this.type = {
      props.sourcesDirectory(s)
      this
    }

    def run: Int = {
      Gatling.fromMap(props.build)
    }
}
