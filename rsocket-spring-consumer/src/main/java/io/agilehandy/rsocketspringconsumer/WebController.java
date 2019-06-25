/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.agilehandy.rsocketspringconsumer;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Haytham Mohamed
 **/

@RestController
@Log4j2
public class WebController {
	private final RSocketRequester requester;

	public WebController(RSocketRequester requester) {
		this.requester = requester;
	}

	@GetMapping("/response/{str}")
	public Mono<String> requestResponse(@PathVariable("str") String str) {
		return requester
				.route("response")
				.data(str)
				.retrieveMono(String.class)
				.log()
				;
	}
}
