package com.byka.germanstandard.controller;

import com.byka.germanstandard.service.FileLoadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/load")
@Slf4j
public class FileLoaderController {
    @Autowired
    private FileLoadService fileLoadService;

    @PostMapping(value = "")
    public void loadFile(@RequestPart(value = "file", required = true) MultipartFile file) {
        try {
            fileLoadService.load(file.getInputStream());
        } catch (IOException e) {
            log.warn("Cannot close input stream because of exception", e);
        }
    }
}
