package models;

import play.data.validation.Constraints.Required;

public class UserForm {
	@Required
	public String username;
	@Required
	public String password;
}
