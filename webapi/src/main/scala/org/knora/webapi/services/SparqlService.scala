/*
 * NIE-INE Project, Basel University
 * author: André Fatton
 *
 */

package org.knora.webapi.services


import java.io.FileOutputStream

import org.w3.banana._
import java.net.URL

import akka.http.scaladsl.model._
import akka.stream.scaladsl.{Source, StreamConverters}
import akka.util.ByteString
import org.w3.banana.jena.Jena
import org.w3.banana.jena.io.JenaRDFWriter
import org.w3.banana.io.{RDFWriter, Turtle}
import akka.http.scaladsl.model.HttpHeader.ParsingResult
import akka.http.scaladsl.model.MediaType.customWithFixedCharset
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.headers.{HttpCookie, `Content-Type`}

import scala.util.Try

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
        //val result: Iterator[Rdf#Triple] = resultGraph.triples.iterator.map { triple =>
          //  triple
        //}
        //result.toList.toString()
    }
}


import org.w3.banana.jena.JenaModule

object SPARQLJenaService extends SPARQLService with JenaModule {
    import sparqlOps._
    import sparqlHttp.sparqlEngineSyntax._

    object MediaNewTypes {
        val `text/turtle` = MediaType.customWithOpenCharset("text/turtle", "turtle")}

    // TODO: switch endpoint to a setting
    val ep = new URL("http://localhost:3030/knora-test/query")
    def sel(q: String): String = select(q, ep)
    def cons(q: String): Rdf#Graph = construct(q, ep)
    def turt(q: String): Unit = testturtle(q, ep)

    def testturtle(query: String, endpoint: URL): Unit = {
        val sparqlquery = parseConstruct(query).get
        val resultGraph: Rdf#Graph = endpoint.executeConstruct(sparqlquery).get
        val os = new FileOutputStream("/home/afa/tmp/turtletest.ttl") // of course a bad idea
        wr.turtleWriter.write(resultGraph, os, "tmp")
        os.close()
        // val byteSource: Source[ByteString, Unit] = StreamConverters.asOutputStream()
        //   .mapMaterializedValue(os => wr.turtleWriter.write(resultGraph, os, "test"))
        //HttpResponse(entity = HttpEntity(
        //    ContentType(MediaNewTypes.`text/turtle`, HttpCharsets.`UTF-8`),
        //    byteSource))
    }
}