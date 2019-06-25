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
package io.agilehandy.rsocketraw;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Haytham Mohamed
 **/
public class ProducerAcceptor extends AbstractRSocket {

	@Override
	public Mono<Payload> requestResponse(Payload payload) {
		return Mono.just(
				DefaultPayload.create(
				"Hello " + payload.getDataUtf8() + "!"
				));
	}

	@Override
	public Flux<Payload> requestStream(Payload payload) {
		Stream stream = Stream.generate(() -> "Hello " + payload.getDataUtf8() + " @ " + Instant.now());
		return Flux.fromStream(stream)
				.delayElements(Duration.ofSeconds(1))
				.map(str -> DefaultPayload.create((String)str))
				;
	}

	@Override
	public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
		return Flux.from(payloads)
				.delayElements(Duration.ofSeconds(1))
				.map(payload -> payload.getDataUtf8())
				.map(str -> "Hello " + str + " @ " + Instant.now())
				.map(str -> DefaultPayload.create((String)str))
				;
	}

}
