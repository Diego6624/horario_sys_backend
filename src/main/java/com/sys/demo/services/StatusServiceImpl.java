package com.sys.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sys.demo.entities.Status;
import com.sys.demo.repositories.HoraryRepository;
import com.sys.demo.repositories.StatusRepository;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public List<Status> listar() {
        return statusRepository.findAll();
    }

}