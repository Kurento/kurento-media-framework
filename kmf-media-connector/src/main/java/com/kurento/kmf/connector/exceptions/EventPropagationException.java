/*
 * (C) Copyright 2013 Kurento (http://kurento.org/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package com.kurento.kmf.connector.exceptions;


/**
 * Exception to be used when an event that has been received form the media
 * server can't be propagated to the client
 *
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.2.3
 *
 */
public class EventPropagationException extends MediaConnectorException {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public EventPropagationException() {
		super();
	}

	/**
	 *
	 * @param msg
	 */
	public EventPropagationException(String msg) {
		super(msg);
	}

	/**
	 *
	 * @param msg
	 * @param cause
	 */
	public EventPropagationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 *
	 * @param cause
	 */
	public EventPropagationException(Throwable cause) {
		super(cause);
	}

}
