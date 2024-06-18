package com.web.movie.controller;

import com.web.movie.entity.Country;
import com.web.movie.entity.Genre;
import com.web.movie.entity.enumType.FilmAccessType;
import com.web.movie.entity.enumType.FilmType;
import com.web.movie.payload.dto.FilmDTO;
import com.web.movie.service.iterface.ICountryService;
import com.web.movie.service.iterface.IFilmService;
import com.web.movie.service.iterface.IGenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {
    private final IFilmService filmService;
    private final IGenreService genreService;
    private final ICountryService countryService;

    @GetMapping("/home-page")
    public ResponseEntity<?> getHomePage(){
        try{
            Page<FilmDTO> singleMovie = filmService.getFilmsByType(FilmType.PHIM_LE, FilmAccessType.FREE, true, 1, 6);
            Page<FilmDTO> seriesMovie = filmService.getFilmsByType(FilmType.PHIM_BO, FilmAccessType.FREE, true, 1, 6);
            Page<FilmDTO> theaterMovie = filmService.getFilmsByType(FilmType.PHIM_CHIEU_RAP, FilmAccessType.FREE, true, 1, 6);
            List<Genre> genres = genreService.findAllGenre();
            List<Country> countries = countryService.findAllCountry();
            Map<String, Object> response = new HashMap<>();
            response.put("singleMovie", singleMovie.getContent());
            response.put("seriesMovie", seriesMovie.getContent());
            response.put("theaterMovie", theaterMovie.getContent());
            response.put("genres", genres);
            response.put("countries", countries);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/store")
    public ResponseEntity<?> getStoreMoviePage(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer limit
    ){
        try{
            Page<FilmDTO> response = filmService.getFilmsByAccessType(FilmAccessType.PAID, true, page, limit);
            Map<String, Object> filmResponse = new HashMap<>();
            filmResponse.put("currentPage", page);
            filmResponse.put("pageData", response);
            return new ResponseEntity<>(filmResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
