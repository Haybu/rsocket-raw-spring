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
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author Haytham Mohamed
 **/

@Component
@ConditionalOnBean(RSocketProducer.class)
@Log4j2
public class RSocketConsumer implements ApplicationRunner {

	private Mono<RSocket> rSocket;

	public RSocketConsumer() {
		log.info("Consumer constructor");
	}

	@PostConstruct
	public void connect() {
		rSocket = RSocketFactory.connect()
				.transport(TcpClientTransport.create(7000))
				.start()
				;
	}

	public void requestResponse() {
		rSocket.flatMap(socket -> socket.requestResponse(DefaultPayload.create("Spring")))
				.map(payload -> payload.getDataUtf8())
				.doOnNext(log::info)
				.subscribeOn(Schedulers.newSingle("other"))
				.subscribe()
				;
	}

	public void requestStream() {
		rSocket.flatMapMany(socket -> socket.requestStream(DefaultPayload.create("Spring Stream")))
				.map(payload -> payload.getDataUtf8())
				.doOnNext(log::info)
				.subscribeOn(Schedulers.newSingle("other"))
				.subscribe()
				;
	}

	public void requestChannel() {
		// Stream of random integers between 0 and 10
		IntStream intStr = new Random().ints(0,10);
		Stream<Integer> stream = intStr.boxed();
		Flux<Payload> flux = Flux.fromStream(stream)
				.delayElements(Duration.ofSeconds(1))
				.map(i -> DefaultPayload.create("Spring Stream element (" + i + ")"));

		rSocket.flatMapMany(socket -> socket.requestChannel(flux))
				.map(payload -> payload.getDataUtf8())
				.doOnNext(log::info)
				.subscribeOn(Schedulers.newSingle("other"))
				.subscribe()
		;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("RSocket consumer app executed!");

		//this.requestResponse();
		//this.requestStream();
		this.requestChannel();
	}

}
