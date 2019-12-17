package com.datapath.moldova.loader;

import com.datapath.moldova.loader.mapps.MappsHandler;
import com.datapath.moldova.loader.mtender.MTenderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;

@SpringBootApplication
//@EnableScheduling
public class LoaderApplication implements CommandLineRunner {

    @Autowired
    private MappsHandler mappsHandler;
    @Autowired
    private MTenderHandler mTenderHandler;

    public static void main(String[] args) {
        SpringApplication.run(LoaderApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new OffsetDateTimeToDateConverter());
        converters.add(new DateToOffsetDateTimeConverter());
        return new MongoCustomConversions(converters);
    }

    @Override
    public void run(String... args) throws Exception {
//        mappsHandler.handle();
        mTenderHandler.handle();
    }

    @WritingConverter
    public static class OffsetDateTimeToDateConverter implements Converter<OffsetDateTime, Date> {


        @Override
        public Date convert(OffsetDateTime source) {
            if (source == null) return null;
            return Date.from(source.toInstant());
        }
    }

    @ReadingConverter
    public static class DateToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {

        @Override
        public OffsetDateTime convert(Date source) {
            if (source == null) return null;
            return ofInstant(ofEpochMilli(source.getTime()), systemDefault()).atOffset(ZoneOffset.UTC);
        }
    }

}
