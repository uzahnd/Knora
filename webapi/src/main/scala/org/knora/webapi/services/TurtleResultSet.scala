package org.knora.webapi.services

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model._
import org.w3.banana.jena.io.JenaRDFWriter
import org.w3.banana.jena.Jena._
import org.knora.webapi.routing.v1.LDPRoute.{`application/turtle`, utf8}

case class TurtleResultSet(graph: Rdf#Graph)

object TurtleResultSetMarshaller
    extends GenericMarshallers
        with PredefinedToEntityMarshallers
        with PredefinedToResponseMarshallers
        with PredefinedToRequestMarshallers {

    val wr = JenaRDFWriter

    def toTurtle(graph: Rdf#Graph): String = {
        wr.turtleWriter.asString(graph, "test").get
    }

    val opaqueTurtleMarshalling = Marshalling.Opaque(() ⇒ "")

    val opaqueTurtleMarshaller: ToResponseMarshaller[TurtleResultSet] = Marshaller.opaque[TurtleResultSet, HttpResponse]
        { res ⇒ HttpResponse(200, entity = res.graph.toString) }
    val withFixedCharsetTurtleMarshaller: ToResponseMarshaller[TurtleResultSet] = Marshaller.withFixedContentType[TurtleResultSet, HttpResponse](`application/turtle`)
        { res ⇒ HttpResponse(200, entity = toTurtle(res.graph))}

    implicit val turtleResultSetMarshaller: ToResponseMarshaller[TurtleResultSet] = Marshaller.oneOf[TurtleResultSet, HttpResponse](opaqueTurtleMarshaller,
        withFixedCharsetTurtleMarshaller)
}