package de.codecentric.appdynamics.apache;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ScoreboardParserTest {

    private Scoreboard scoreboard;

    @Before
    public void setUp() {
	scoreboard = new Scoreboard();
    }

    @Test
    public void shouldReturnEmptyScoreboard() throws Exception {
	scoreboard.parseScoreboard("");
	assertEquals(0, scoreboard.Total);
    }

    @Test
    public void shouldReturnIdleScoreboard() throws Exception {
	scoreboard.parseScoreboard("____");
	assertEquals(4, scoreboard.Total);
	assertEquals(4, scoreboard.Idle);
    }

    @Test
    public void shouldReturnScoreboardWith2KeepAlive() throws Exception {
	scoreboard.parseScoreboard("KK");
	assertEquals(2, scoreboard.KeepAlive);
    }
}
