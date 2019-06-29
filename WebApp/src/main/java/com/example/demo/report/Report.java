package com.example.demo.report;

import com.example.demo.model.MoviesSummary;
import com.example.demo.model.Ticket;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Report {

    public List<MoviesSummary> getMoviesSummary(List<Ticket> tickets){

        return tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getMovie))
                .entrySet()
                .stream()
                .map(entry ->
                    new MoviesSummary(entry.getKey().getTitle(),
                            entry.getValue().stream()
                                    .mapToDouble(Ticket::getPrice)
                                    .sum()))
                .sorted(Comparator.comparingDouble(MoviesSummary::getIncome).reversed())
                .collect(Collectors.toList());
    }
}
