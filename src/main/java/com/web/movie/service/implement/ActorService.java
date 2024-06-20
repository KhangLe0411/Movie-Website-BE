package com.web.movie.service.implement;

import com.web.movie.entity.Actor;
import com.web.movie.repository.ActorRepository;
import com.web.movie.service.iterface.IActorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActorService implements IActorService {
    private final ActorRepository actorRepository;
    @Override
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }
}
