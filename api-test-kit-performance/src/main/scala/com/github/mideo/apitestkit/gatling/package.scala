package com.github.mideo.apitestkit

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

package object gatling {

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
}
