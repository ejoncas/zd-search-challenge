package com.zendesk.codingchallenge.search.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.zendesk.codingchallenge.search.model.*;
import com.zendesk.codingchallenge.search.repository.EntityRepository;
import com.zendesk.codingchallenge.search.repository.JsonArrayEntityRepository;
import com.zendesk.codingchallenge.search.service.InMemorySearchService;
import com.zendesk.codingchallenge.search.service.SearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;

@Configuration
public class SearchConfig {

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public EntityRepository<User> userRepository() {
        return new JsonArrayEntityRepository<>(User.class, new ClassPathResource("file_datastore/users-long.json"), gson());
    }

    @Bean
    public EntityRepository<Ticket> ticketRepository() {
        return new JsonArrayEntityRepository<>(Ticket.class, new ClassPathResource("file_datastore/tickets.json"), gson());
    }

    @Bean
    public EntityRepository<Organisation> organisationRepository() {
        return new JsonArrayEntityRepository<>(Organisation.class, new ClassPathResource("file_datastore/organisations.json"), gson());
    }

    @Bean
    public SearchService<User> userSearchService() {
        return new InMemorySearchService<>(userRepository().findAll());
    }


    @Bean
    public SearchService<Ticket> ticketSearchService() {
        return new InMemorySearchService<>(ticketRepository().findAll());
    }

    @Bean
    public SearchService<Organisation> organisationSearchService() {
        return new InMemorySearchService<>(organisationRepository().findAll());
    }

    @Bean
    public Map<EntityType, SearchService<? extends BaseEntity>> searchServiceMap() {
        return ImmutableMap.of(
                EntityType.ORGANISATION, organisationSearchService(),
                EntityType.TICKET, ticketSearchService(),
                EntityType.USER, userSearchService()
        );
    }

}
