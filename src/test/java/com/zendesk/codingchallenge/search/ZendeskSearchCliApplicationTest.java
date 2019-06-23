package com.zendesk.codingchallenge.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false",
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"
})
/**
 * This is a really basic integration test to make sure everything is wired correctly.
 *
 * Lots of search scenarios have been tested in {@link com.zendesk.codingchallenge.search.service.InMemorySearchServiceTest}
 */
public class ZendeskSearchCliApplicationTest {

    @Autowired
    private Shell shell;

    @Test
    public void testHelp() {
        Object result = shell.evaluate(() -> "help");
        assertThat(result.toString(), containsString("search: Search for Tickets, Users or Organizations"));
        assertThat(result.toString(), containsString("showfields: List all searchable fields"));
    }

    //This is the example in the instructions
    @Test
    public void searchById() {
        Object result = shell.evaluate(() -> "search user _id 71");
        assertThat(result.toString(), containsString("Prince Hinton"));
        assertThat(result.toString(), containsString("c972bb41-94aa-4f20-bc93-e63dbfe8d9ca"));
        //Make sure external entities are also there
        assertThat(result.toString(), containsString("A Catastrophe in Micronesia"));
        assertThat(result.toString(), containsString("A Drama in Wallis and Futuna Islands"));
        assertThat(result.toString(), containsString("A Drama in Australia"));
    }


    @Test
    public void searchTicketByOrganisationId() {
        Object result = shell.evaluate(() -> "search ticket organization_id 555");
        //ID of the only ticket in that organisation
        assertThat(result.toString(), containsString("7523607d-d45c-4e3a-93aa-419402e64d73"));
        //Make sure external entities are also there
        assertThat(result.toString(), containsString("User:33"));
        assertThat(result.toString(), containsString("User:20"));
    }

    @Test
    public void searchOrganizationByTags() {
        Object result = shell.evaluate(() -> "search organisation tags Erickson");
        //ID of the only ticket in that organisation
        assertThat(result.toString(), containsString("_id => 119"));
        //Make sure external entities are also there
        assertThat(result.toString(), containsString("User:1005"));
        assertThat(result.toString(), containsString("Ticket:5613ffcb-8a33-4341-9be7-1534ae1050bc"));
    }

}