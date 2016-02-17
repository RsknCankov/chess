import java.net.URISyntaxException;
import java.sql.SQLException;

import models.Database;
import play.*;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application app) {
		Logger.info("Application has started");
	}

	@Override
	public void onStop(Application app) {
		Logger.info("Application shutdown...");
	}

}