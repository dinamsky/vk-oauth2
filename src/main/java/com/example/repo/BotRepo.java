package com.example.repo;

import com.example.entity.Bot;
import org.springframework.data.repository.CrudRepository;

public interface BotRepo extends CrudRepository<Bot,Integer> {
}
