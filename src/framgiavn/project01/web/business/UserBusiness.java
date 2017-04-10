package framgiavn.project01.web.business;

import framgiavn.project01.web.model.User;

public interface UserBusiness {
	public User findByUserId(Integer user_id) throws Exception;

	public User findByUsername(String username) throws Exception;
}
