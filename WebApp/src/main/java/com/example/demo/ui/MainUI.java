package com.example.demo.ui;

import com.example.demo.model.*;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.TicketRepository;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.data.provider.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringUI(path = "")
public class MainUI extends UI {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Page.getCurrent().setTitle("Zapisy na seans");

        HorizontalLayout root = new HorizontalLayout();
        root.setSpacing(true);
        VerticalLayout verticalLayout = new VerticalLayout();

        List<Movie> movies = new ArrayList<>();

        Grid<Movie> movieGrid = new Grid<>("Wyszukane filmy:");
        movieGrid.addColumn(Movie::getId).setCaption("ID").setWidth(50);
        movieGrid.addColumn(Movie::getTitle).setCaption("Tytuł").setWidth(250);
        movieGrid.addColumn(Movie::getType).setCaption("Gatunek").setWidth(150);
        movieGrid.addColumn(m -> m.getIs3D() == true ? "tak" : "nie").setCaption("3D").setWidth(70);
        movieGrid.addColumn(Movie::getShowedFrom).setCaption("Data premiery").setWidth(150);
        movieGrid.addColumn(Movie::getShowedTo).setCaption("Data zakończenia").setWidth(150);
        movieGrid.addColumn(Movie::getRating).setCaption("Ocena").setWidth(100);
        movieGrid.setWidth("980");
        movieGrid.setHeight("280");
        ListDataProvider<Movie> provider = DataProvider.ofCollection(movies);
        movieGrid.setDataProvider(provider);
        movieGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        List<Ticket> tickets = ticketRepository.findAll();

        Grid<Ticket> ticketGrid = new Grid<>("Wszystkie bilety:");
        ticketGrid.addColumn(Ticket::getId).setCaption("ID").setWidth(50);
        ticketGrid.addColumn(Ticket::getPrice).setCaption("Cena").setWidth(100);
        ticketGrid.addColumn(t -> t.getClient().getName()).setCaption("Klient").setWidth(200);
        ticketGrid.addColumn(t -> t.getMovie().getTitle()).setCaption("Film").setWidth(250);
        ticketGrid.setWidth("600");
        ticketGrid.setHeight("280");
        ListDataProvider<Ticket> provider2 = DataProvider.ofCollection(tickets);
        ticketGrid.setDataProvider(provider2);
        ticketGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        TextField name = new TextField("Imię i nazwisko:");

        ComboBox<ClientType> clientType = new ComboBox<>("Typ klienta:");
        clientType.setEmptySelectionAllowed(false);
        clientType.setDataProvider(DataProvider.ofCollection(Arrays.asList(ClientType.values())));

        ComboBox<MovieType> movieType = new ComboBox<>("Gatunek filmu:");
        movieType.setEmptySelectionAllowed(false);
        movieType.setDataProvider(DataProvider.ofCollection(Arrays.asList(MovieType.values())));
        movieType.addValueChangeListener(event -> {

            List<Movie> foundMovies = movieRepository.findAll()
                    .stream()
                    .filter(m -> m.getType().equals(movieType.getValue()))
                    .sorted(Comparator.comparingInt(Movie::getRating).reversed())
                    .collect(Collectors.toList());

            movies.clear();
            movies.addAll(foundMovies);
            provider.refreshAll();
        });

        DateField showedOn = new DateField("Data seansu:");
        showedOn.addValueChangeListener(event -> {

            List<Movie> foundMovies = movieRepository.findAll()
                    .stream()
                    .filter(m -> m.getType().equals(movieType.getValue()))
                    .filter(m ->
                        m.getShowedFrom().compareTo(showedOn.getValue()) < 0 &&
                                m.getShowedTo().compareTo(showedOn.getValue()) > 0)
                    .sorted(Comparator.comparingInt(Movie::getRating).reversed())
                    .collect(Collectors.toList());

            movies.clear();
            movies.addAll(foundMovies);
            provider.refreshAll();
        });

        CheckBox is3D = new CheckBox("3D");
        is3D.addValueChangeListener(event -> {

            List<Movie> foundMovies = movieRepository.findAll()
                    .stream()
                    .filter(m -> m.getType().equals(movieType.getValue()))
                    .filter(m ->
                            m.getShowedFrom().compareTo(showedOn.getValue()) < 0 &&
                                    m.getShowedTo().compareTo(showedOn.getValue()) > 0)
                    .filter(m -> m.getIs3D() == is3D.getValue())
                    .sorted(Comparator.comparingInt(Movie::getRating).reversed())
                    .collect(Collectors.toList());

            movies.clear();
            movies.addAll(foundMovies);
            provider.refreshAll();
        });

        Button buyTicket = new Button("Kup bilet");
        buyTicket.addClickListener(event -> {

            String nameText = name.getValue();
            ClientType clientTypeText = clientType.getValue();

            if(nameText.equals("") || clientTypeText == null){
                Notification.show("Nie podano wszystkich danych!", "", Notification.Type.ERROR_MESSAGE);
            }

            else if(movieGrid.getSelectedItems().size() != 1){
                Notification.show("Wybierz dokładnie jeden film!", "", Notification.Type.ERROR_MESSAGE);
            }

            else{
                Double price = getPrice(clientType.getValue());
                Client client = clientRepository.save(new Client(0l, nameText, clientTypeText));
                Movie movie = movieGrid.getSelectedItems().iterator().next();

                Ticket ticket = ticketRepository.save(new Ticket(0l, price, client, movie));
                Notification.show("Bilet został zakupiony!", "", Notification.Type.HUMANIZED_MESSAGE);

                tickets.add(ticket);
                provider2.refreshAll();
            }
        });

        verticalLayout.addComponents(name, clientType, movieType, showedOn, is3D, buyTicket);

        Label emptyLabel = new Label();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.addComponents(movieGrid, emptyLabel, ticketGrid);

        root.addComponents(verticalLayout, horizontalLayout, verticalLayout1);
        setContent(root);
    }

    private Double getPrice(ClientType clientType){

        if(clientType.equals(ClientType.ADULT)){
            return 24.99;
        }

        else if(clientType.equals(ClientType.STUDENT)){
            return 19.99;
        }

        else if(clientType.equals(ClientType.CHILD)){
            return 14.99;
        }

        return 0.0;
    }
}
