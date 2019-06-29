package com.example.demo.tests;

import com.example.demo.model.*;
import com.example.demo.report.Report;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MoviesSummaryTest {

    @Test
    public void getSummary(){
        LocalDate now = LocalDate.now();

        Client client1 = new Client(0l, "Anna Nowak", ClientType.ADULT);
        Client client2 = new Client(0l, "Piotr Kowalski", ClientType.STUDENT);
        Client client3 = new Client(0l, "Alicja Kowalczyk", ClientType.CHILD);

        Movie movie1 = new Movie(0l, "Piraci z Karaibów", MovieType.ADVENTURE, true, now.minusMonths(1), now.plusMonths(1), 8);
        Movie movie3 = new Movie(0l, "To właśnie miłość", MovieType.ROMANTIC, true, now.minusMonths(1), now.plusMonths(1), 10);

        Ticket ticket1 = new Ticket(0l, 24.99, client1, movie3);
        Ticket ticket2 = new Ticket(0l, 19.99, client2, movie3);
        Ticket ticket3 = new Ticket(0l, 14.99, client3, movie1);

        List<Ticket> tickets = new ArrayList<>();
        tickets.addAll(Arrays.asList(ticket1, ticket2, ticket3));

        List<MoviesSummary> given = new Report().getMoviesSummary(tickets);

        List<MoviesSummary> expected = new ArrayList<>();
        expected.add(new MoviesSummary("To właśnie miłość", 44.98));
        expected.add(new MoviesSummary("Piraci z Karaibów", 14.99));

        assertEquals(given, expected);
    }
}
