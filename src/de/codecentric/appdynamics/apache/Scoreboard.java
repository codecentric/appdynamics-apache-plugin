package de.codecentric.appdynamics.apache;

/**
 * Model class for counting all the different values mod_status can output.
 */
public final class Scoreboard {
    int ReadingRequest;
    int SendingReply;
    int KeepAlive;
    int DNSLookup;
    int ClosingConnection;
    int Logging;
    int GracefullyFinishing;
    int CleaningUp;
    int StartingUp;
    int Idle;
    int Total;

    public Scoreboard() {
    }

    public Scoreboard(String value) {
	parseScoreboard(value);
    }

    /**
     * Will try to parse a scoreboard String and return the resulting Scoreboard. Invalid (or yet
     * unknown) values will be skipped.
     * 
     * @param value
     * @return
     */
    public Scoreboard parseScoreboard(String value) {
	Total = value.length();

	for (int i = 0; i < value.length(); i++) {
	    switch (value.charAt(i)) {
	    case 'I':
		CleaningUp++;
		break;

	    case 'C':
		ClosingConnection++;
		break;

	    case 'S':
		StartingUp++;
		break;

	    case 'R':
		ReadingRequest++;
		break;

	    case 'W':
		SendingReply++;
		break;

	    case 'K':
		KeepAlive++;
		break;

	    case 'D':
		DNSLookup++;
		break;

	    case 'L':
		Logging++;
		break;

	    case 'G':
		GracefullyFinishing++;
		break;

	    default:
		Idle++;
	    }
	}
	return this;
    }
}
