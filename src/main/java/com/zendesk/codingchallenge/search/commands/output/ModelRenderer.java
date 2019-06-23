package com.zendesk.codingchallenge.search.commands.output;

import com.zendesk.codingchallenge.search.model.BaseEntity;
import com.zendesk.codingchallenge.search.model.Organisation;
import com.zendesk.codingchallenge.search.model.Ticket;
import com.zendesk.codingchallenge.search.model.User;
import com.zendesk.codingchallenge.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class ModelRenderer {

    private final SearchService<User> userSearch;
    private final SearchService<Organisation> organisationSearch;
    private final SearchService<Ticket> ticketSearch;

    @Autowired
    public ModelRenderer(SearchService<User> userSearch,
                         SearchService<Organisation> organisationSearch,
                         SearchService<Ticket> ticketSearch) {
        this.userSearch = userSearch;
        this.organisationSearch = organisationSearch;
        this.ticketSearch = ticketSearch;
    }

    public String render(User user) {
        TableOutput output = new PojoReflectorTableOutput(user);
        //Print related entities in summary mode
        //Print organisation. There should only be only one really
        addSummaryItems(output, "Organisations where User belongs",
                organisationSearch.search("_id", String.valueOf(user.getOrganizationId())),
                Organisation::getName);
        //Print tickets created by user
        addSummaryItems(output, "Tickets submitted by user",
                ticketSearch.search("submitter_id", String.valueOf(user.getId())),
                Ticket::getSubject);
        addSummaryItems(output, "Tickets assigned to the user",
                ticketSearch.search("assignee_id", String.valueOf(user.getId())),
                Ticket::getSubject);
        return output.render();
    }

    public String render(Organisation organisation) {
        TableOutput output = new PojoReflectorTableOutput(organisation);
        //Print related entities in summary mode
        //Print users.
        addSummaryItems(output, "Users in Organisation",
                userSearch.search("organization_id", String.valueOf(organisation.getId())),
                User::getName);
        //Print tickets.
        addSummaryItems(output, "Tickets in Organisation",
                ticketSearch.search("organization_id", String.valueOf(organisation.getId())),
                Ticket::getSubject);
        return output.render();
    }

    public String render(Ticket ticket) {
        TableOutput output = new PojoReflectorTableOutput(ticket);
        //Print related entities in summary mode
        //Print organisation. There should be only one really
        addSummaryItems(output, "Organisation owning this ticket",
                organisationSearch.search("_id", String.valueOf(ticket.getOrganizationId())),
                Organisation::getName);
        //Print user. There should be only one really.
        addSummaryItems(output, "Assigned User",
                userSearch.search("_id", String.valueOf(ticket.getAssigneeId())),
                User::getName);
        addSummaryItems(output, "Submitting User",
                userSearch.search("_id", String.valueOf(ticket.getSubmitterId())),
                User::getName);
        return output.render();
    }

    public String render(Object object) {
        if (object instanceof Ticket)
            return render((Ticket) object);
        if (object instanceof User)
            return render((User) object);
        if (object instanceof Organisation)
            return render((Organisation) object);
        //Default implementation
        return object.toString();
    }

    public <T extends BaseEntity> void addSummaryItems(
            TableOutput output, String title, List<T> entities, Function<T, String> summaryLineSupplier) {
        output.addTableBreak(title);
        entities.forEach(e -> output.addRow(e.toString(), summaryLineSupplier.apply(e)));
    }

}
