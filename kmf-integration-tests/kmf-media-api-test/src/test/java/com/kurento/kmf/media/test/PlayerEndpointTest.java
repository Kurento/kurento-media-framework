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
package com.kurento.kmf.media.test;

import static com.kurento.kmf.media.test.RtpEndpoint2Test.URL_SMALL;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.kurento.kmf.common.exception.KurentoException;
import com.kurento.kmf.media.PlayerEndpoint;
import com.kurento.kmf.media.events.EndOfStreamEvent;
import com.kurento.kmf.media.events.MediaEventListener;
import com.kurento.kmf.media.test.base.AsyncEventManager;
import com.kurento.kmf.media.test.base.MediaPipelineBaseTest;

/**
 * {@link PlayerEndpoint} test suite.
 *
 * <p>
 * Methods tested:
 * <ul>
 * <li>{@link PlayerEndpoint#getUri()}
 * <li>{@link PlayerEndpoint#play()}
 * <li>{@link PlayerEndpoint#pause()}
 * <li>{@link PlayerEndpoint#stop()}
 * </ul>
 * <p>
 * Events tested:
 * <ul>
 * <li>{@link PlayerEndpoint#addEndOfStreamListener(MediaEventListener)}
 * </ul>
 *
 *
 * @author Ivan Gracia (igracia@gsyc.es)
 * @version 1.0.0
 *
 */
public class PlayerEndpointTest extends MediaPipelineBaseTest {

	private PlayerEndpoint player;

	@Before
	public void setupMediaElements() throws KurentoException {
		player = pipeline.newPlayerEndpoint(URL_SMALL).build();
	}

	@After
	public void teardownMediaElements() {
		player.release();
	}

	/**
	 * start/pause/stop sequence test
	 */
	@Test
	public void testPlayer() {
		player.play();
		player.pause();
		player.stop();
	}

	@Test
	public void testEventEndOfStream() throws InterruptedException {

		AsyncEventManager<EndOfStreamEvent> async = new AsyncEventManager<>(
				"EndOfStream event");

		player.addEndOfStreamListener(async.getMediaEventListener());

		player.play();

		async.waitForResult();
	}

	@Test
	public void testCommandGetUri() {
		Assert.assertTrue(URL_SMALL.equals(player.getUri()));
	}

}
