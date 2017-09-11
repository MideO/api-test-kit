package com.github.mideo.apitestkit.gatling

import com.github.mideo.apitestkit.ApiTestKitProperties


object PerformanceTestConfig {

  lazy val properties:ApiTestKitProperties  = ApiTestKitProperties.create()
  private def load(s:String): Unit = {
    properties.load(s)
  }


  lazy val PropertyFile: String = {
    properties.getString("test.property.file")
  }

  lazy val SuccessfulRequestPercentage: Int = {
    properties.getInt("successful.request.percentage", 98)
  }

  lazy val NumberOfUniqueUsers: Int = {
    properties.getInt("number.of.unique.users", 5)
  }

  lazy val RequestPerSecond: Int = {
    properties.getInt("requests.per.second", 1)
  }

  lazy val Url: String = {
    properties.getString("test.api.url", "http://localhost")
  }

  lazy val Duration: Int = {
    properties.getInt("duration.minutes", 10)

  }


  def get(s:String):String = {
    properties.getString(s)
  }

  def withPropertyFile(s:String): this.type = {
    load(s)
    properties.set("test.property.file", s)

    this
  }

  def withSuccessfulRequestPercentage(n: Int): this.type = {
    properties.set("successful.request.percentage", n)
    this
  }

  def withRequestPerSecond(n: Int): this.type = {
    properties.set("requests.per.second", n)
    this
  }

  def withNumberOfUniqueUsers(n: Int): this.type = {
    properties.set("number.of.unique.users", n)
    this
  }

  def forUrl(url: String): this.type = {
    if (!url.toLowerCase.startsWith("http")) {
      throw new AssertionError(s"$url must start with http protocol")
    }
    properties.set("test.api.url", url)
    this
  }

  def forDurationInMinutes(n: Int): this.type = {
    properties.set("duration.minutes", n)
    this
  }
}
