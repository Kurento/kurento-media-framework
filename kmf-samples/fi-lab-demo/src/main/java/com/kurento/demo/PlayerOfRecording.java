/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
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
package com.kurento.demo;

import com.kurento.kmf.content.HttpPlayerHandler;
import com.kurento.kmf.content.HttpPlayerService;
import com.kurento.kmf.content.HttpPlayerSession;
import com.kurento.kmf.media.HttpGetEndpoint;
import com.kurento.kmf.media.MediaPipeline;
import com.kurento.kmf.media.PlayerEndpoint;

/**
 * HTTP Player (plays a file previously recorded in the filesystem of KMS).
 * 
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @since 4.3.1
 */
@HttpPlayerService(path = "/playerOfRecording", useControlProtocol = false)
public class PlayerOfRecording extends HttpPlayerHandler {

	@Override
	public void onContentRequest(HttpPlayerSession contentSession)
			throws Exception {
		// Media Pipeline
		MediaPipeline mp = contentSession.getMediaPipelineFactory().create();
		contentSession.releaseOnTerminate(mp);

		// Media Elements: Player Endpoint, HTTP Endpoint
		PlayerEndpoint playerEndpoint = mp.newPlayerEndpoint(
				"file:///tmp/recording").build();
		contentSession.setAttribute("player", playerEndpoint);
		HttpGetEndpoint httpEndpoint = mp.newHttpGetEndpoint().terminateOnEOS()
				.build();

		// Connections
		playerEndpoint.connect(httpEndpoint);

		// Start content session
		contentSession.start(httpEndpoint);
	}

	@Override
	public void onContentStarted(HttpPlayerSession contentSession) {
		PlayerEndpoint playerEndpoint = (PlayerEndpoint) contentSession
				.getAttribute("player");
		playerEndpoint.play();
	}

}
