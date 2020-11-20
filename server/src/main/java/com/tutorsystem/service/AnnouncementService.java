package com.tutorsystem.service;

import com.tutorsystem.model.Announcement;
import com.tutorsystem.model.Config;
import com.tutorsystem.repository.AnnouncementRepository;
import com.tutorsystem.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository repository;

    public List<Announcement> getAll(){
        return repository.findAll();
    }

    public List<Announcement> lastFive() {
        return repository.findAll().stream().sorted().limit(5).collect(Collectors.toList());
    }

    public List<Announcement> getFive(int page) {
        if(page < 0) return null;
        return repository.findAll().stream().sorted().skip((page-1)*5).limit(5).collect(Collectors.toList());
    }

}
