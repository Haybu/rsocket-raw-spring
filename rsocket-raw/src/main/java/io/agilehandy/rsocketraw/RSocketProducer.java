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

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;

/**
 * @author Haytham Mohamed
 **/

@Component
@Log4j2
public class RSocketProducer  {

	RSocketProducer() {
		log.info("Producer constructor");
		RSocketFactory.receive()
				.acceptor((setup, sender) -> Mono.just(new ProducerAcceptor()))
				.transport(TcpServerTransport.create(7000))
				.start()
				.block()
		;
	}

}
