/*
 * NIE-INE Project, Basel University
 * author: André Fatton
 *
 */

package org.knora.webapi.services

import org.w3.banana._
import java.net.URL
import akka.http.scaladsl.marshalling.{ToResponseMarshallable, ToResponseMarshaller}
import org.w3.banana.jena.io.JenaRDFWriter
import scala.concurrent.ExecutionContext

trait SPARQLDeps
    extends RDFModule
        with RDFOpsModule
        with SparqlOpsModule
        with SparqlHttpModule

trait SPARQLService extends SPARQLDeps { self =>

    import ops._
    import sparqlOps._
    import sparqlHttp.sparqlEngineSyntax._
    val wr = JenaRDFWriter

    //type Rdf = Jena

    def select(query: String, endpoint: URL): String = {

        val sparqlquery = parseSelect(query).get
        val result: Rdf#Solutions = endpoint.executeSelect(sparqlquery).get
        val rows: Iterator[Rdf#Solution] = result.iterator.map { row =>
            row }
        rows.toList.toString
        //JsArray(rows.toVector)
    }

    def construct(query: String, endpoint: URL): Rdf#Graph = {

        val sparqlquery = parseConstruct(query).get
        val resultGraph: Rdf#Graph = endpoint.executeConstruct(sparqlquery).get
        resultGraph
    }
}


import org.w3.banana.jena.JenaModule

object SPARQLJenaService extends SPARQLService with JenaModule {
    import sparqlOps._
    import sparqlHttp.sparqlEngineSyntax._
    import TurtleResultSetMarshaller._
    val ec = ExecutionContext.global

    // TODO: switch endpoint to a setting
    val ep = new URL("http://localhost:3030/knora-test/query")
    def sel(q: String): String = select(q, ep)
    def cons(q: String): Rdf#Graph = construct(q, ep)
    def turt(q: String): ToResponseMarshallable = testturtle(q, ep)(turtleResultSetMarshaller, ec)

    def testturtle(query: String, endpoint: URL)(implicit _marshaller: ToResponseMarshaller[TurtleResultSet], ec: ExecutionContext): ToResponseMarshallable = {
        val sparqlquery = parseConstruct(query).get
        val resultGraph: Rdf#Graph = endpoint.executeConstruct(sparqlquery).get
        val ts = TurtleResultSet.apply(resultGraph)
        ToResponseMarshallable.apply(ts)(_marshaller)
    }
}