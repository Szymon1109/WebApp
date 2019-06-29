package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.model.MoviesSummary;
import com.example.demo.report.Report;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/incomes")
    public List<MoviesSummary> getMoviesSummary(){
        return new Report().getMoviesSummary(ticketRepository.findAll());
    }

    @GetMapping("/movies")
    public List<Movie> getMovies(){
        return movieRepository.findAll();
    }
}
