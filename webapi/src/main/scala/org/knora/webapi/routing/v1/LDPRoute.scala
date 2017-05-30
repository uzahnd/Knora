/*
 * NIE-INE Project, Basel University
 * author: André Fatton
 *
 */

package org.knora.webapi.routing.v1

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.MediaType.{WithFixedCharset, WithOpenCharset}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{MediaTypeNegotiator, Route}
import org.knora.webapi._
import org.knora.webapi.routing.Authenticator
import org.knora.webapi.services.JenaJsonMarshaller
import org.knora.webapi.util.InputValidation
import akka.http.scaladsl.settings.ParserSettings
import akka.http.scaladsl.settings.ServerSettings
import org.knora.webapi.services.SPARQLJenaService._



/**
  * Akka HTTP Route for a Linked Data Platform Interface
  */

object LDPRoute extends JenaJsonMarshaller with Authenticator {

    // custom TURTLE media type:
    val utf8 = HttpCharsets.`UTF-8`
    val `application/turtle`: WithFixedCharset =
        MediaType.customWithFixedCharset("application", "turtle", utf8)



    def knoraApiPath(_system: ActorSystem, settings: SettingsImpl, log: LoggingAdapter): Route = {


    implicit val system: ActorSystem = _system
    implicit val executionContext = system.dispatcher
        implicit val timeout = settings.defaultTimeout
        val responderManager = system.actorSelection ("/user/responderManager")
        val ldpEncodings = Seq(MediaRange(MediaTypes.`application/xml`),
            MediaRange(MediaTypes.`application/json`),
            MediaRange(`application/turtle`))
        val parserSettings = ParserSettings(system).withCustomMediaTypes(`application/turtle`)
        val serverSettings = ServerSettings(system).withParserSettings(parserSettings)

    path ("ldp" / Segment) {
        value =>
            get {
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
                      LIMIT 2000"""
                    complete(
                        encoding.toString match {
                            case "application/turtle" => turt(q) //HttpResponse(StatusCodes.OK, entity = "turtle")
                            case "application/xml" => HttpResponse(StatusCodes.OK, entity = "xml")
                            case "application/json" => cons(q) //HttpResponse(StatusCodes.OK, entity = "json")
                            case _ => HttpResponse(StatusCodes.OK, entity = encoding.toString)
                        }
                    )
                }
            }
    }
    }
}