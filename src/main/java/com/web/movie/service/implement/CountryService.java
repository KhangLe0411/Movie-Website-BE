package com.web.movie.service.implement;

import com.web.movie.entity.Country;
import com.web.movie.exception.ResourceNotFoundException;
import com.web.movie.repository.CountryRepository;
import com.web.movie.service.iterface.ICountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryService implements ICountryService {
    private final CountryRepository countryRepository;
    @Override
    public Country getCountryBySlug(String slug) {
        return countryRepository.findBySlug(slug)
                .orElseThrow(()->new ResourceNotFoundException("Không tìm thấy quốc gia có slug: " + slug));
    }

    @Override
    public List<Country> findAllCountry() {
        return countryRepository.findAll();
    }
}
