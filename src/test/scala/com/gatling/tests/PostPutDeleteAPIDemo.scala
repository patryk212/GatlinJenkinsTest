package com.gatling.tests

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PostPutDeleteAPIDemo extends Simulation {

  //Protocol

  val httpProtocol = http
    .baseUrl("https://reqres.in/api")

  //Scenario
  val createUserScn = scenario("Create User")
    .exec(
      http("create user req")
        .post("/users")
        .asJson
        .body(RawFileBody("user.json"))
        //.body(StringBody(
        //  """
        //    |{
        //    |   "name": "morpheus",
        //    |   "job": "leader"
        //    |}
        //    |""".stripMargin)).asJson
        .check(status is 201))
    .pause(1)

  val updateUserScn = scenario("Update User")
    .exec(
      http("update user")
        .put("/users/2")
        .body(RawFileBody("user.json")).asJson
        .check(
          status is 200
        )
    )
    .pause(1)

  val deleteUserScn = scenario("Delete User")
    .exec(
      http("delete user")
        .delete("/users/2")
        .check(
          status is 204
        )
    )
    .pause(1)
  //Setup
  setUp(
    createUserScn.inject(rampUsers(5).during(5)),
    updateUserScn.inject(rampUsers(5).during(3)),
    deleteUserScn.inject(rampUsers(3).during(1))
  ).protocols(httpProtocol)
}


