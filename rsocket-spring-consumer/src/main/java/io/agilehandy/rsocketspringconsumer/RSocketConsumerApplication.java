package io.agilehandy.rsocketspringconsumer;

import io.netty.buffer.PooledByteBufAllocator;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@SpringBootApplication
public class RSocketConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RSocketConsumerApplication.class, args);
	}

	@Bean
	RSocket rSocket() {
		return RSocketFactory.connect()
				.metadataMimeType("message/x.rsocket.routing.v0")
				.dataMimeType(MimeTypeUtils.TEXT_PLAIN_VALUE)
				.frameDecoder(PayloadDecoder.ZERO_COPY)
				.transport(TcpClientTransport.create("localhost", 7001))
				.start()
				.block();
	}

	@Bean
	public RSocketStrategies rsocketStrategies() {
		return RSocketStrategies.builder()
				.decoder(StringDecoder.allMimeTypes())
				.encoder(CharSequenceEncoder.allMimeTypes())
				.dataBufferFactory(new NettyDataBufferFactory(PooledByteBufAllocator.DEFAULT))
				.build();
	}

	@Bean
	public RSocketRequester rSocketRequester(RSocketStrategies rsocketStrategies, RSocket rSocket) {
		MimeType ROUTING = new MimeType("message", "x.rsocket.routing.v0");
		return RSocketRequester.wrap(rSocket
				, MimeTypeUtils.TEXT_PLAIN
				, ROUTING
				, rsocketStrategies);
	}
}
