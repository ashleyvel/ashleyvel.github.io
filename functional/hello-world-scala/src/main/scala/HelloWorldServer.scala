// src/main/scala/HelloWorldServer.scala

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn

object HelloWorldServer {

  def main(args: Array[String]): Unit = {
    // Create an ActorSystem
    implicit val system = ActorSystem(Behaviors.empty, "helloWorldSystem")
    implicit val executionContext = system.executionContext
    // Define list of items (fruits and desserts)
    val items = List(
      "Apple", "Banana", "Carrot Cake", "Chocolate Donut", "Cherry", "Chocolate Eclair", "Fig", "Grapes"
    )
    // Define the routes
    val route =
      path("greet" / Segment) { person =>
        get {
          complete(s"Hello, $person!")
        }
      } ~
      pathSingleSlash {
        get {
          complete(s"Hello!")
        }
      } ~
      path("sort") {
        get {
          // Separate fruits and desserts
          val (fruits, desserts) = items.partition(item => !item.toLowerCase.contains("cake") && !item.toLowerCase.contains("chocolate"))
          
          // Alphabetically sort both lists
          val sortedFruits = fruits.sorted
          val sortedDesserts = desserts.sorted

          // Construct the response
          val response =
            s"""
               |  Fruits: ${sortedFruits.mkString("[\"", "\",\"", "\"]")},
               |  Desserts: ${sortedDesserts.mkString("[\"", "\",\"", "\"]")}
               |""".stripMargin

          complete(response)
        }
      }

    // Start the server
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println("Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // Keep the server running until user presses return
    bindingFuture
      .flatMap(_.unbind()) // Unbind from the port
      .onComplete(_ => system.terminate()) // Terminate the system when done
  }
}