package com.example.springbootstudy.repos;

import com.example.springbootstudy.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface MessageRepo extends JpaRepository<Message,Integer> {
    List<Message> findByTag(String tag);



}
