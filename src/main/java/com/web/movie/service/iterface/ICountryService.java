package com.web.movie.service.iterface;

import com.web.movie.entity.Country;

import java.util.List;

public interface ICountryService {
    Country getCountryBySlug(String slug);
    List<Country> findAllCountry();
}
