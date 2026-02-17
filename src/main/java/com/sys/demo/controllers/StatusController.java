package com.sys.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sys.demo.entities.Status;
import com.sys.demo.services.StatusService;

@RestController
@RequestMapping("/api/horaries/status")
@CrossOrigin("*")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping
    public List<Status> listar() {
        return statusService.listar();
    }
}