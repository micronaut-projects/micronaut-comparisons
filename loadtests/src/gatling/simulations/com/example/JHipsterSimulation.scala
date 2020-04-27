package com.example

import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

class JHipsterSimulation extends Simulation {
  val basicAuth = "Basic YWRtaW46YWRtaW4=" // Basic admin:admin
  val httpConf = http
    .baseUrl(baseUrl)
    .header("Accept", "application/json")

  def userCount: Int = getProperty("loadtests.users", "1000").toInt
  def baseUrl: String = getProperty("loadtests.baseurl", "http://localhost:8080")
  def rampDuration: Int = getProperty("loadtests.rampduration", "10").toInt
  def testDuration: Int = getProperty("loadtests.duration", "60").toInt

  /** * Before ***/
  before {
    println(s"baseUrl: ${baseUrl}")
    println(s"Running test with ${userCount} users")
    println(s"Ramping users over ${rampDuration} seconds")
    println(s"Maximum test duration: ${testDuration} seconds")
  }

  /** * Helper Methods ***/
  protected def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  def getAccount() = {
    exec(http("getAccount")
      .get("/api/account")
      .header("Authorization", basicAuth)
      .check(jsonPath("$.login").is("admin"))
      .check(status.is(200)))
  }

  def getHealth() = {
    exec(http("health")
      .get("/management/health")
      .check(jsonPath("$.status").is("UP"))
      .check(status.is(200)))
  }

  def createBankAccount() = {
      exec(http("createBankAccount")
          .post("/api/bank-accounts")
          .header("Content-Type", "application/json; charset=utf-8")
          .header("Authorization", basicAuth)
          .body(StringBody("{\"name\": \"Bank of Evil\", \"balance\": 200}"))
          .check(status.is(201)))
  }

  /** * Scenario Design ***/
  val scn = scenario("JHipster Load Test")
    .forever() {
      exec(getHealth())
        .exec(getAccount())
        .exec(createBankAccount())
    }

  /** * Setup Load Simulation ***/
  setUp(
    scn.inject(
      nothingFor(5 seconds),
      rampUsers(userCount).during(rampDuration seconds))
  ).protocols(httpConf).maxDuration(testDuration seconds)

  /** * After ***/
  after {
    println("Stress test completed")
  }
}
