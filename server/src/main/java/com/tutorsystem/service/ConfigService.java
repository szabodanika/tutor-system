package com.tutorsystem.service;

import com.tutorsystem.model.Config;
import com.tutorsystem.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    @Autowired
    private ConfigRepository repository;

    public Config get() {
        if (this.repository.findAll().isEmpty()) {
            this.init();
        }
        return this.repository.findAll().get(0);
    }

    public void init() {
        Config config = new Config();
        repository.save(config);
    }

}
