/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.jaxrs.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Manuel de la Peña
 */
@Path("/example")
public class NameResource {

	@GET
	@Produces("text/plain")
	public String sayHello() {
		return "Hello, world!";
	}

	@Path("/{name}")
	public NameGreeter sayHello(@PathParam("name") String name) {
		return new NameGreeter(name);
	}

	public static class NameGreeter {
		private String _name;

		public NameGreeter(String name) {
			_name = name;
		}

		@GET
		@Produces({"application/json", "application/xml", "text/plain"})
		public Name sayHello() {
			return new Name("Hello, " + _name + "!");
		}

		@Path("/{name}")
		public NameGreeter sayHelloToName(@PathParam("name") String name) {
			return new NameGreeter(_name + " y " + name);
		}

		@PUT
		@Consumes("text/plain")
		public String receiveName(Name name) {
			return name.getName();
		}

	}

	@XmlRootElement
	public static class Name {

		private String _name;

		public Name(String name) {
			_name = name;
		}

		public String getName() {
			return _name;
		}

	}

}