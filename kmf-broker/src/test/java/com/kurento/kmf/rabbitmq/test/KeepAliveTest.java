package com.kurento.kmf.rabbitmq.test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.kurento.kmf.jsonrpcconnector.DefaultJsonRpcHandler;
import com.kurento.kmf.jsonrpcconnector.KeepAliveManager;
import com.kurento.kmf.jsonrpcconnector.Transaction;
import com.kurento.kmf.jsonrpcconnector.internal.message.Request;
import com.kurento.kmf.media.MediaPipeline;
import com.kurento.kmf.media.factory.MediaPipelineFactory;
import com.kurento.kmf.rabbitmq.client.JsonRpcClientRabbitMq;
import com.kurento.kmf.rabbitmq.server.JsonRpcServerRabbitMq;

public class KeepAliveTest {

	private static final int NUM_KEEP_ALIVES = 5;

	private static Logger log = LoggerFactory.getLogger(KeepAliveTest.class);

	private CountDownLatch latch = new CountDownLatch(NUM_KEEP_ALIVES);

	public class EchoJsonRpcHandler extends DefaultJsonRpcHandler<JsonObject> {

		private int numObjects = 0;

		@Override
		public void handleRequest(Transaction transaction,
				Request<JsonObject> request) throws Exception {

			if ("keepAlive".equals(request.getMethod())) {
				log.info("keepAlive");
				latch.countDown();
				transaction.sendResponse(null);
				return;
			}

			if ("create".equals(request.getMethod())) {
				transaction.sendResponse("ObjectId_" + numObjects);
				numObjects++;
			}
		}
	}

	@Test
	public void test() throws TException, IOException, InterruptedException {

		System.setProperty(KeepAliveManager.KEEP_ALIVE_INTERVAL_TIME_PROPERTY,
				"1000");

		log.info("Starting server");
		JsonRpcServerRabbitMq server = new JsonRpcServerRabbitMq(
				new EchoJsonRpcHandler());

		server.start();
		log.info("Server started");

		long initTime = System.nanoTime();

		log.info("Starting client");
		JsonRpcClientRabbitMq client = new JsonRpcClientRabbitMq();

		MediaPipelineFactory mpf = new MediaPipelineFactory(client);
		MediaPipeline pipeline = mpf.create();

		checkKeepAlives(initTime, NUM_KEEP_ALIVES * 1000,
				(NUM_KEEP_ALIVES + 1) * 1000);

		// There are two pipelines and NUM_KEEP_ALIVES are submited in the half
		// of the time
		initTime = System.nanoTime();
		latch = new CountDownLatch(NUM_KEEP_ALIVES);
		MediaPipeline pipeline2 = mpf.create();
		checkKeepAlives(initTime, NUM_KEEP_ALIVES * 1000 / 2,
				(NUM_KEEP_ALIVES + 1) * 1000 / 2);

		client.close();

		log.info("Client finished");

		server.destroy();

		log.info("Server finished");

	}

	private void checkKeepAlives(long initTime, long minTime, long maxTime)
			throws InterruptedException {
		if (!latch.await(1500, TimeUnit.SECONDS)) {
			Assert.fail("Timeout of 15s waiting for keepAlives");
		} else {
			long duration = ((System.nanoTime() - initTime) / 1000000);

			Assert.assertTrue(
					"Waiting time should be greather than estimated keepAlive time ("
							+ duration + " > " + minTime + ")",
					duration > minTime);

			Assert.assertTrue(
					"Waiting time should be less than 1 keepAlive more than estimated keepAlive time ("
							+ duration + " < " + maxTime + ")",
					duration < maxTime);

		}
	}
}
