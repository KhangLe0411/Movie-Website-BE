package com.web.movie.controller;

import com.web.movie.entity.enumType.FilmAccessType;
import com.web.movie.entity.enumType.FilmType;
import com.web.movie.payload.dto.EpisodeDTO;
import com.web.movie.payload.dto.FilmDTO;
import com.web.movie.payload.dto.FilmDetailDTO;
import com.web.movie.payload.dto.ReviewDTO;
import com.web.movie.service.iterface.IEpisodeService;
import com.web.movie.service.iterface.IFilmService;
import com.web.movie.service.iterface.IReviewService;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FilmController {
    private final IFilmService filmService;
    private final IReviewService reviewService;
    private final IEpisodeService episodeService;

    @GetMapping("/film/single-movie")
    public ResponseEntity<?> getSingleMoviePage(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer limit
    ){
        try{
            Page<FilmDTO> response = filmService.getFilmsByType(FilmType.PHIM_LE, FilmAccessType.FREE, true, page, limit);
            Map<String, Object> filmResponse = new HashMap<>();
            filmResponse.put("currentPage", page);
            filmResponse.put("pageData", response);
            return new ResponseEntity<>(filmResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/film/series-movie")
    public ResponseEntity<?> getSeriesMoviePage(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer limit
    ){
        try{
            Page<FilmDTO> response = filmService.getFilmsByType(FilmType.PHIM_BO, FilmAccessType.FREE, true, page, limit);
            Map<String, Object> filmResponse = new HashMap<>();
            filmResponse.put("currentPage", page);
            filmResponse.put("pageData", response);
            return new ResponseEntity<>(filmResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/film/theater-movie")
    public ResponseEntity<?> getTheaterMoviePage(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer limit
    ){
        try{
            Page<FilmDTO> response = filmService.getFilmsByType(FilmType.PHIM_CHIEU_RAP, FilmAccessType.FREE, true, page, limit);
            Map<String, Object> filmResponse = new HashMap<>();
            filmResponse.put("currentPage", page);
            filmResponse.put("pageData", response);
            return new ResponseEntity<>(filmResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/film/{id}/{slug}")
    public ResponseEntity<?> getMovieDetailPage(
            @PathVariable Integer id,
            @PathVariable String slug,
            @RequestParam(required = false, defaultValue = "FREE") String type
    ){
        try{
            FilmDetailDTO film;
            if(type.equals("FREE")){
                film = filmService.findFilmByIdAndSlug(id, slug, FilmAccessType.FREE);
            } else {
                film = filmService.findFilmByIdAndSlug(id, slug, FilmAccessType.PAID);
            }
            List<ReviewDTO> reviews = reviewService.getReviewsOfFilm(id);
            List<EpisodeDTO> episodes = episodeService.getEpisodesByFilmId(id);
            Map<String, Object> filmResponse = new HashMap<>();
            filmResponse.put("pageData", film);
            filmResponse.put("review", reviews);
            filmResponse.put("episodes", episodes);
            return new ResponseEntity<>(filmResponse, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/film/watch/{id}/{slug}")
    public ResponseEntity<?> getWatchingMoviePage(@PathVariable Integer id,
                                                  @PathVariable String slug,
                                                  @RequestParam(required = false, defaultValue = "1") String tap){
        try{
            List<EpisodeDTO> episodes = episodeService.getEpisodesByFilmId(id);
            EpisodeDTO currentEpisode = episodeService.getEpisodeByOrderEpisode(id, true, tap);

            Map<String, Object> episodeResponse = new HashMap<>();
            episodeResponse.put("episodes", episodes);
            episodeResponse.put("currentEpisode", currentEpisode);
            return new ResponseEntity<>(episodeResponse, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
