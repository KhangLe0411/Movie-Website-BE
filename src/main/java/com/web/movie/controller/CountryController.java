package com.web.movie.controller;

import com.web.movie.entity.Country;
import com.web.movie.entity.enumType.FilmAccessType;
import com.web.movie.payload.dto.FilmDTO;
import com.web.movie.service.iterface.ICountryService;
import com.web.movie.service.iterface.IFilmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CountryController {
    private final IFilmService filmService;
    private final ICountryService countryService;

    @GetMapping("/country/{slug}")
    public ResponseEntity<?> getCountryMoviePage(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer limit,
            @PathVariable String slug
    ){
        try {
            log.info(slug);
            Page<FilmDTO> response = filmService.getFilmsOfCountry(slug, FilmAccessType.FREE, true, page, limit);
            Country country = countryService.getCountryBySlug(slug);
            Map<String, Object> filmResponse = new HashMap<>();
            filmResponse.put("currentPage", page);
            filmResponse.put("pageData", response);
            filmResponse.put("country", country);
            return new ResponseEntity<>(filmResponse, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
