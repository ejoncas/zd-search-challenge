package com.zendesk.codingchallenge.search.commands.output;

import com.zendesk.codingchallenge.search.model.Organisation;
import com.zendesk.codingchallenge.search.model.Ticket;
import com.zendesk.codingchallenge.search.model.User;
import com.zendesk.codingchallenge.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//TODO Fix repeated code about table breaks
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
        output.addTableBreak("Organisations where User belongs");
        organisationSearch.search("_id", String.valueOf(user.getOrganizationId())).forEach(
                organisation -> output.addRow(organisation.toString(), organisation.getName())
        );
        //Print tickets created by user
        output.addTableBreak("Tickets submitted by user");
        ticketSearch.search("submitter_id", String.valueOf(user.getId())).forEach(
                ticket -> output.addRow(ticket.toString(), ticket.getSubject())
        );
        output.addTableBreak("Tickets assigned to the user");
        ticketSearch.search("assignee_id", String.valueOf(user.getId())).forEach(
                ticket -> output.addRow(ticket.toString(), ticket.getSubject())
        );
        return output.render();
    }

    public String render(Organisation organisation) {
        TableOutput output = new PojoReflectorTableOutput(organisation);
        //Print related entities in summary mode
        //Print users.
        output.addTableBreak("Users in Organisation");
        userSearch.search("organization_id", String.valueOf(organisation.getId())).forEach(
                user -> output.addRow(user.toString(), user.getName())
        );
        //Print tickets.
        output.addTableBreak("Tickets in Organisation");
        ticketSearch.search("organization_id", String.valueOf(organisation.getId())).forEach(
                ticket -> output.addRow(ticket.toString(), ticket.getSubject())
        );
        return output.render();
    }

    public String render(Ticket ticket) {
        TableOutput output = new PojoReflectorTableOutput(ticket);
        //Print related entities in summary mode
        //Print organisation. There should be only one really
        output.addTableBreak("Organisation owning this ticket");
        organisationSearch.search("_id", String.valueOf(ticket.getOrganizationId())).forEach(
                organisation -> output.addRow(organisation.toString(), organisation.getName())
        );
        //Print user. There should be only one really.
        output.addTableBreak("Assigned User");
        userSearch.search("_id", String.valueOf(ticket.getAssigneeId())).forEach(
                user -> output.addRow(user.toString(), user.getName())
        );
        output.addTableBreak("Submitting User");
        userSearch.search("_id", String.valueOf(ticket.getSubmitterId())).forEach(
                user -> output.addRow(user.toString(), user.getName())
        );
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


}
