package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Service
public class InitService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @PostConstruct
    public void init() {

        LocalDate now = LocalDate.now();

        Client client1 = clientRepository.save(new Client(0l, "Anna Nowak", ClientType.ADULT));
        Client client2 = clientRepository.save(new Client(0l, "Piotr Kowalski", ClientType.STUDENT));
        Client client3 = clientRepository.save(new Client(0l, "Alicja Kowalczyk", ClientType.CHILD));

        Movie movie1 = movieRepository.save(
                new Movie(0l, "Piraci z Karaibów", MovieType.ADVENTURE, true, now.minusMonths(1), now.plusMonths(1), 8));
        Movie movie2 = movieRepository.save(
                new Movie(0l, "Kac Vegas", MovieType.COMEDY, true, now.minusDays(15), now.plusDays(45), 7));
        Movie movie3 = movieRepository.save(
                new Movie(0l, "To właśnie miłość", MovieType.ROMANTIC, true, now.minusMonths(1), now.plusMonths(1), 10));
        Movie movie4 = movieRepository.save(
                new Movie(0l, "Harry Potter i Zakon Feniksa", MovieType.ADVENTURE, true, now.minusDays(25), now.plusMonths(1), 10));
        Movie movie5 = movieRepository.save(
                new Movie(0l, "Głupi i głupszy", MovieType.COMEDY, false, now.minusDays(25), now.plusDays(35), 8));
        Movie movie6 = movieRepository.save(
                new Movie(0l, "Pretty Woman", MovieType.ROMANTIC, false, now.minusDays(20), now.plusDays(40), 8));
        Movie movie7 = movieRepository.save(
                new Movie(0l, "Władca Pierścieni", MovieType.ADVENTURE, false, now.minusDays(20), now.plusDays(40), 7));
        Movie movie8 = movieRepository.save(
                new Movie(0l, "Akademia policyjna", MovieType.COMEDY, false, now.minusDays(15), now.plusDays(45), 9));
        Movie movie9 = movieRepository.save(
                new Movie(0l, "Czas na miłość", MovieType.ROMANTIC, false, now.minusDays(35), now.plusDays(25), 10));

        ticketRepository.save(new Ticket(0l, 24.99, client1, movie3));
        ticketRepository.save(new Ticket(0l, 19.99, client2, movie3));
        ticketRepository.save(new Ticket(0l, 14.99, client3, movie1));
    }
}
