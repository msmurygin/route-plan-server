package ru.ltmanagement.common.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ltmanagement.common.controller.response.VersionResponse;
import ru.ltmanagement.configuration.ControllerURL;


@RestController
public class VersionController {

    @Value("${api.version}")
    private String version;


    @GetMapping(path = ControllerURL.VERSION_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VersionResponse> version(){
        return ResponseEntity.ok(new VersionResponse(version));
    }
}
