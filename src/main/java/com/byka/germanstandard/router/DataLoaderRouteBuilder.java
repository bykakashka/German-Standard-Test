package com.byka.germanstandard.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;

@Component
public class DataLoaderRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("{{com.byka.hotfolder}}?include=.*.csv&moveFailed=.error&move=.done")
                .process(exchange -> {
                    File fileToUpload = exchange.getIn().getBody(File.class);
                    exchange.getMessage().setBody(new FileInputStream(fileToUpload));
                })
                .to("bean:fileLoadService");
    }
}
