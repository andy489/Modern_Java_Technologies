package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommandCreatorTest {

    @Test
    public void testCommandCreationWithNoArguments() {
        String command = "test";
        Command cmd = CommandCreator.newCommand(command);

        assertEquals(command, cmd.command(), "unexpected command returned for command 'test'");
        assertNotNull(cmd.arguments(), "command arguments should not be null");
        assertEquals(0, cmd.arguments().length, "unexpected command arguments count");
    }

    @Test
    public void testCommandCreationWithOneArgument() {
        String command = "test one";
        Command cmd = CommandCreator.newCommand(command);

        assertEquals(command.split(" ")[0], cmd.command(), "unexpected command returned for command 'test one'");
        assertNotNull(cmd.arguments(), "command arguments should not be null");
        assertEquals(1, cmd.arguments().length, "unexpected command arguments count");
        assertEquals(command.split(" ")[1], cmd.arguments()[0], "unexpected argument returned for command 'test one'");
    }

    @Test
    public void testCommandCreationWithTwoArguments() {
        String command = "test one two";
        Command cmd = CommandCreator.newCommand(command);

        assertEquals(command.split(" ")[0], cmd.command(), "unexpected command returned for command 'test one two'");
        assertNotNull(cmd.arguments(), "command arguments should not be null");
        assertEquals(2, cmd.arguments().length, "unexpected command arguments count");
        assertEquals("one", cmd.arguments()[0], "unexpected argument returned for command 'text one two'");
        assertEquals("two", cmd.arguments()[1], "unexpected argument returned for command 'text one two'");
    }

}
