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
package com.kurento.kmf.jsonrpcconnector.internal.server;

import java.io.IOException;
import java.util.concurrent.ScheduledFuture;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kurento.kmf.jsonrpcconnector.client.Continuation;
import com.kurento.kmf.jsonrpcconnector.internal.JsonRpcRequestSenderHelper;
import com.kurento.kmf.jsonrpcconnector.internal.client.AbstractSession;
import com.kurento.kmf.jsonrpcconnector.internal.message.Request;
import com.kurento.kmf.jsonrpcconnector.internal.message.Response;

public abstract class ServerSession extends AbstractSession {

	private final SessionsManager sessionsManager;
	private JsonRpcRequestSenderHelper rsHelper;
	private String transportId;
	private ScheduledFuture<?> closeTimerTask;

	// TODO Make this configurable
	private long reconnectionTimeoutInMillis = 10000;

	public ServerSession(String sessionId, Object registerInfo,
			SessionsManager sessionsManager, String transportId) {

		super(sessionId, registerInfo);

		this.transportId = transportId;
		this.sessionsManager = sessionsManager;
	}

	public abstract void handleResponse(Response<JsonElement> response);

	public String getTransportId() {
		return transportId;
	}

	public void setTransportId(String transportId) {
		this.transportId = transportId;
	}

	@Override
	public void close() throws IOException {
		this.sessionsManager.remove(this.getSessionId());
	}

	protected void setRsHelper(JsonRpcRequestSenderHelper rsHelper) {
		this.rsHelper = rsHelper;
	}

	@Override
	public <R> R sendRequest(String method, Class<R> resultClass)
			throws IOException {
		return rsHelper.sendRequest(method, resultClass);
	}

	@Override
	public <R> R sendRequest(String method, Object params, Class<R> resultClass)
			throws IOException {
		return rsHelper.sendRequest(method, params, resultClass);
	}

	@Override
	public JsonElement sendRequest(String method) throws IOException {
		return rsHelper.sendRequest(method);
	}

	@Override
	public JsonElement sendRequest(String method, Object params)
			throws IOException {
		return rsHelper.sendRequest(method, params);
	}

	@Override
	public void sendRequest(String method, JsonObject params,
			Continuation<JsonElement> continuation) {
		rsHelper.sendRequest(method, params, continuation);
	}

	@Override
	public void sendNotification(String method, Object params,
			Continuation<JsonElement> continuation) throws IOException {
		rsHelper.sendNotification(method, params, continuation);
	}

	@Override
	public void sendNotification(String method, Object params)
			throws IOException {
		rsHelper.sendNotification(method, params);
	}

	@Override
	public void sendNotification(String method) throws IOException {
		rsHelper.sendNotification(method);
	}

	public Response<JsonElement> sendRequest(Request<JsonObject> request)
			throws IOException {
		return rsHelper.sendRequest(request);
	}

	public void sendRequest(Request<JsonObject> request,
			Continuation<Response<JsonElement>> continuation)
			throws IOException {
		rsHelper.sendRequest(request, continuation);
	}

	public void setCloseTimerTask(ScheduledFuture<?> closeTimerTask) {
		this.closeTimerTask = closeTimerTask;
	}

	public ScheduledFuture<?> getCloseTimerTask() {
		return closeTimerTask;
	}

	@Override
	public void setReconnectionTimeout(long reconnectionTimeoutInMillis) {
		this.reconnectionTimeoutInMillis = reconnectionTimeoutInMillis;
	}

	public long getReconnectionTimeoutInMillis() {
		return reconnectionTimeoutInMillis;
	}
}
