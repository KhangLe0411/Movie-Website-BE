package com.web.movie.service.implement;

import com.web.movie.entity.Director;
import com.web.movie.repository.DirectorRepository;
import com.web.movie.service.iterface.IDirectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorService implements IDirectorService {
    private final DirectorRepository directorRepository;
    @Override
    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }
}
