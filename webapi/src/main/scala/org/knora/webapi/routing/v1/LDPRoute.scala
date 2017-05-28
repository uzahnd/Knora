/*
 * NIE-INE Project, Basel University
 * author: André Fatton
 *
 */

package org.knora.webapi.routing.v1

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{MediaRange, MediaTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{MediaTypeNegotiator, Route}
import org.knora.webapi._
import org.knora.webapi.routing.Authenticator
import org.knora.webapi.services.JenaJsonMarshaller
import org.knora.webapi.services.SPARQLJenaService._
import org.knora.webapi.util.InputValidation

/**
  * Akka HTTP Route for a Linked Data Platform Interface
  */

object LDPRoute extends JenaJsonMarshaller with Authenticator {

    def knoraApiPath(_system: ActorSystem, settings: SettingsImpl, log: LoggingAdapter): Route = {


    implicit val system: ActorSystem = _system
    implicit val executionContext = system.dispatcher
        implicit val timeout = settings.defaultTimeout
        val responderManager = system.actorSelection ("/user/responderManager")
        val ldpEncodings = Seq(MediaRange(MediaTypes.`application/xml`),MediaRange(MediaTypes.`application/json`),MediaNewTypes.`text/turtle`)

    path ("ldp" / Segment) {
        value =>
            get { //name =>
                (get & extract(_.request.headers)){ requestHeaders =>
                    val mediaTypeNegotiator = new MediaTypeNegotiator(requestHeaders)
                    val encoding = mediaTypeNegotiator
                        .acceptedMediaRanges
                        .intersect(ldpEncodings)
                        .headOption
                        .getOrElse(MediaRange(MediaTypes.`application/json`))

                    val iri = InputValidation.toIri (value, () => throw BadRequestException (s"Invalid IRI $value") )

                    val q: String = """CONSTRUCT {?subject ?predicate ?object}
                      WHERE {
                        ?subject ?predicate ?object
                      }
                      LIMIT 20"""
                    complete(
                        encoding.toString match {
                            case "application/xml" => "xml"
                            case "application/json" => "json"
                            case "text/turtle" => "turtle"
                            case _ => encoding.toString
                        })
                    //cons(q))
                }
            }
    }
    }
}