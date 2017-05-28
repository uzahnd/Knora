/*
 * NIE-INE Project, Basel University
 * author: André Fatton
 *
 */
package org.knora.webapi.services

import org.knora.webapi.services.SPARQLJenaService.{Rdf, ops}
import org.w3.banana.jena.JenaOps
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._



trait JenaJsonMarshaller extends JenaOps with SprayJsonSupport with DefaultJsonProtocol {

    // Rdf#Lang
    implicit object langJsonFormat extends RootJsonFormat[Rdf#Lang]{
        def write(lang: Rdf#Lang) = JsString(fromLang(lang))
        def read(value: JsValue) = value match {
            case JsString(lang) => makeLang(lang.toString())
            case _ => deserializationError("Rdf#Lang expected")
        }
    }

    // Rdf#Literal
    implicit object literalJsonFormat extends RootJsonFormat[Rdf#Literal]{
        def write(literal: Rdf#Literal) = {
            val (lf, uri, langOpt) = fromLiteral(literal)
            var fields = Map( "lexicalform" -> JsString(lf))
            if (uri != null) {fields += ("datatype" -> JsString(uri.toString()))}
            if (langOpt.isDefined) {fields += ("lang" ->(JsString(langOpt.toString)))}

            JsObject(fields)
        }
        def read(value: JsValue) = ???
    }

    // Rdf#BNode
    implicit object bnodeJsonFormat extends RootJsonFormat[Rdf#BNode]{
        def write(bnode: Rdf#BNode) = JsObject("bnode" -> JsString(fromBNode(bnode)))
        def read(value: JsValue) = ???
    }

    // Rdf#URI
    implicit object uriJsonFormat extends RootJsonFormat[Rdf#URI] {
        def write(uri: Rdf#URI) = JsObject("uri" -> JsString(fromUri(uri)))

        def read(value: JsValue) = ???
    }

    // Rdf#Triple
    implicit object tripleJsonFormat extends RootJsonFormat[Rdf#Triple] {
        def write(triple: Rdf#Triple) = {
            val (s, p, o) = fromTriple(triple)
            JsArray(JsObject("s" -> foldNode(s)(uri => uri.toJson, bn => bn.toJson, lit => lit.toJson)),
                JsObject("p" -> foldNode(p)(uri => uri.toJson, bn => bn.toJson, lit => lit.toJson)),
                    JsObject("o" -> foldNode(o)(uri => uri.toJson, bn => bn.toJson, lit => lit.toJson)))
        }

        def read(value: JsValue) = ???
    }

    // Rdf#Graph
    implicit object graphJsonFormat extends RootJsonFormat[Rdf#Graph] {
        def write(graph: Rdf#Graph) = {
            val triples: Iterable[Rdf#Triple] = getTriples(graph)
            val jsgraph: Iterable[JsValue] = triples map {triple =>
                triple.toJson
            }
            JsObject("graph" -> JsArray(jsgraph.toJson))
        }

        def read(value: JsValue) = ???
    }
}