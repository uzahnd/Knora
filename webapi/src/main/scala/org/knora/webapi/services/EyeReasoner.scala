package org.knora.webapi.services

/**
  * Created by afa on 01.06.17.
  */

import akka.actor.{Actor, Props}
import sys.process._

/**
  * EyeReasoner is a transient actor that handles the starting of the eye reasoner
  * and holds computation state.
  *
  * 1: who will be the sender to this actor?
  * 2: where will EyeReasoner send messages to?
  * 3: How will we configure the eye reasoner? (we should be able to use eye with
  * all the different options).
  * 4: How do we parse the answers from eye?
  * 5: Should we not use an actor at all?
  * 6: Should we explore Akka Streams?
  *
  */



case class EyeQuery(optionString: String)

class EyeReasoner(query: EyeQuery) extends Actor {
    override def receive: Receive = handleQuery(query)

    def handleQuery(query: EyeQuery): Receive = {
        case query: EyeQuery => eye(query)
    }

    def eye(query: EyeQuery):String = {
        println(Thread.currentThread().getName)
        val output = Seq("/home/afa/Eye/eye.sh" ++ query.optionString).!!
        output}
    }


object EyeReasoner {
    def props(query: EyeQuery): Props = Props(new EyeReasoner(query))
    case object ReasonerOptions
}
