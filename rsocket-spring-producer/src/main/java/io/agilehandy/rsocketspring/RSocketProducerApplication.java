package io.agilehandy.rsocketspring;

import io.netty.buffer.PooledByteBufAllocator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.messaging.rsocket.RSocketStrategies;

@SpringBootApplication
public class RSocketProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RSocketProducerApplication.class, args);
	}

	@Bean
	public RSocketStrategies rsocketStrategies() {
		return RSocketStrategies.builder()
				.decoder(StringDecoder.allMimeTypes())
				.encoder(CharSequenceEncoder.allMimeTypes())
				.dataBufferFactory(new NettyDataBufferFactory(PooledByteBufAllocator.DEFAULT))
				.build();
	}

}
