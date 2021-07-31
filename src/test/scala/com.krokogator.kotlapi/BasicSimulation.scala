package com.krokogator.kotlapi

import io.gatling.core.Predef._
import io.gatling.http.Predef.http

import scala.concurrent.duration.DurationInt

class BasicSimulation extends Simulation {
  val httpProtocol = http
    .baseUrl("http://localhost:8080/api")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("Scenario Name")
    .exec(
      http("random_request")
        .get("/coroutinemock")
//        .get("/mock")
    )

  setUp(scn.inject(constantUsersPerSec(100).during(5.seconds)).protocols(httpProtocol))
}