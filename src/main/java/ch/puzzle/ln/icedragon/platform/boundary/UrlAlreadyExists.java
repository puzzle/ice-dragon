package ch.puzzle.ln.icedragon.platform.boundary;

import ch.puzzle.ln.web.rest.errors.BadRequestAlertException;
import ch.puzzle.ln.web.rest.errors.ErrorConstants;

public class UrlAlreadyExists extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UrlAlreadyExists() {
        super(ErrorConstants.PLATFORM_URL_EXISTS_TYPE, "Platform URL already used!", "userManagement", "platformurlexists");
    }
}
