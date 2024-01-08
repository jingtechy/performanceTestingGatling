package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps

class CatFactsSimulation extends Simulation {

  val httpConf: HttpProtocolBuilder = http.baseUrl("https://cat-fact.herokuapp.com/facts/")

  val catFactsEndPoint = "https://cat-fact.herokuapp.com/facts/"

  val getCatFactsScenario: ChainBuilder = exec(http("Get Cat Facts API")
    .get(catFactsEndPoint)
    .header("Content-Type", "application/json")
    .check(status.is(200))
  ).exec(session => {
    // Log custom message
    println(s"${session("requestName").as[String]} request sent successfully!")
    session
  })

  // Execute Cat Facts Api scenario
  val catFactsApiTest: ScenarioBuilder = scenario("Execute Cat Facts Api Scenario")
    .exec(getCatFactsScenario)

  setUp(
    catFactsApiTest
      .inject(constantUsersPerSec(1) during (30 seconds)))
    .protocols(httpConf)
}