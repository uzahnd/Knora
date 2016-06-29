/*
 * Copyright © 2015 Lukas Rosenthaler, Benjamin Geer, Ivan Subotic,
 * Tobias Schweizer, André Kilchenmann, and André Fatton.
 *
 * This file is part of Knora.
 *
 * Knora is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Knora is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with Knora.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.knora.webapi.routing.v1

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import arq.iri
import org.apache.commons.validator.routines.UrlValidator
import org.knora.webapi.{BadRequestException, SettingsImpl}
import org.knora.webapi.messages.v1.responder.groupmessages._
import org.knora.webapi.messages.v1.responder.projectmessages.{ProjectInfoType, ProjectsGetRequestV1}
import org.knora.webapi.routing.{Authenticator, RouteUtilV1}
import org.knora.webapi.util.InputValidation
import spray.routing.Directives._
import spray.routing._

import scala.util.Try

object GroupsRouteV1 extends Authenticator {

    private val schemes = Array("http", "https")
    private val urlValidator = new UrlValidator(schemes)

    def rapierPath(_system: ActorSystem, settings: SettingsImpl, log: LoggingAdapter): Route = {

        implicit val system: ActorSystem = _system
        implicit val executionContext = system.dispatcher
        implicit val timeout = settings.defaultTimeout
        val responderManager = system.actorSelection("/user/responderManager")

        path("v1" / "groups") {
            get {
                requestContext =>
                    val requestMessageTry = Try {
                        val userProfile = getUserProfileV1(requestContext)
                        val params = requestContext.request.uri.query.toMap
                        val infoType = params.getOrElse("infoType", GroupInfoType.SHORT.toString)
                        GroupsGetRequestV1(GroupInfoType.lookup(infoType), Some(userProfile))
                    }
                    RouteUtilV1.runJsonRoute(
                        requestMessageTry,
                        requestContext,
                        settings,
                        responderManager,
                        log
                    )
            }
        } ~
            path("v1" / "groups" / Segment) { value =>
                get {
                    requestContext =>
                        val requestMessageTry = Try {
                            val userProfile = getUserProfileV1(requestContext)
                            val params = requestContext.request.uri.query.toMap
                            val infoType = params.getOrElse("infoType", GroupInfoType.SHORT.toString)
                            val projectIriValue = params.getOrElse("projectIri", "")
                            val projectIri = InputValidation.toIri(projectIriValue, () => throw BadRequestException(s"Invalid project IRI supplied: $projectIriValue"))
                            if (urlValidator.isValid(value)) {
                                /* valid URL */
                                GroupInfoByIRIGetRequest(value, GroupInfoType.lookup(infoType), Some(userProfile))
                            } else {
                                /* not valid URL so I assume it is an username */
                                GroupInfoByNameGetRequest(projectIri, value, GroupInfoType.lookup(infoType), Some(userProfile))
                            }
                        }
                        RouteUtilV1.runJsonRoute(
                            requestMessageTry,
                            requestContext,
                            settings,
                            responderManager,
                            log
                        )
                }
            }
    }
}
