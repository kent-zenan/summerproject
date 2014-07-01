package ac.uk.nottingham.ningboport.model;

/**
 * XML for login request and response. For details about XML format, please see
 * spec.xml
 * 
 * @author Jiaqi LI
 * 
 */
public class XMLLoginComm {

	XMLSession session;
	XMLLogin login;

	public XMLLoginComm(XMLSession s, XMLLogin l) {
		session = s;
		login = l;
	}

	public XMLSession getSession() {
		return session;
	}

	public void setSession(XMLSession session) {
		this.session = session;
	}

	public XMLLogin getLogin() {
		return login;
	}

	public void setLogin(XMLLogin login) {
		this.login = login;
	}

	public boolean isSessionExist() {
		return session.isSessionValid();
	}
}
