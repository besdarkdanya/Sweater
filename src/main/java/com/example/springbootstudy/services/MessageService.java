package com.example.springbootstudy.services;

import com.example.springbootstudy.domain.Message;
import com.example.springbootstudy.repos.MessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    public void deleteMessage(Message message) {
        messageRepo.delete(message);
    }

}
