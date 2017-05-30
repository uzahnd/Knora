package org.knora.webapi.services

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model.{ContentType, HttpCharset, MediaType, MediaTypes}
import org.knora.webapi.services.SPARQLJenaService.MediaNewTypes
import org.w3.banana.jena.io.JenaRDFWriter
import org.w3.banana.jena.Jena._

/**
object JenaTurtleMarshaller
    extends GenericMarshallers
        with PredefinedToEntityMarshallers
        with PredefinedToResponseMarshallers
        with PredefinedToRequestMarshallers { */

    case class TurtleResultSet(graph: Rdf#Graph)
    object TurtleResultSet {

    val wr = JenaRDFWriter

    def toTurtle(graph: Rdf#Graph): String = {
        wr.turtleWriter.asString(graph, "test").get
    }

        val opaqueTurtleMarshalling = Marshalling.Opaque(() ⇒ "")
        val openCharsetTurtleMarshalling = Marshalling.WithOpenCharset(MediaNewTypes.`application/turtle`, (charset: HttpCharset) ⇒ "")
       // val fixedCharsetTurtleMarshalling = Marshalling.WithFixedCharset(`text/xml`, `UTF-8`, () ⇒ personXml)

    val opaqueTurtleMarshaller: Marshaller[TurtleResultSet, String] = Marshaller.opaque[TurtleResultSet, String] { res ⇒ toTurtle(res.graph) }
   // val withFixedCharsetTurtleMarshaller = Marshaller.withFixedCharset[TurtleResultSet, String](MediaNewTypes.`application/turtle`, `UTF-8`) { res ⇒ toTurtle(res) }
    val withOpenCharsetCharsetTurtleMarshaller: Marshaller[TurtleResultSet, String] = Marshaller.withOpenCharset[TurtleResultSet, String](MediaNewTypes.`application/turtle`) {(res, charset) ⇒ toTurtle(res.graph) }

    implicit val turtleResultSetMarshaller: Marshaller[TurtleResultSet, String] = Marshaller.oneOf[TurtleResultSet, String](opaqueTurtleMarshaller,
       // withFixedCharsetTurtleMarshaller,
     withOpenCharsetCharsetTurtleMarshaller)
}