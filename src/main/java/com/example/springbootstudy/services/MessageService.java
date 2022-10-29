package com.example.springbootstudy.services;

import com.example.springbootstudy.domain.Message;
import com.example.springbootstudy.repos.MessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepo messageRepo;

    public void deleteMessage(Message message) {
        messageRepo.delete(message);
    }


}
