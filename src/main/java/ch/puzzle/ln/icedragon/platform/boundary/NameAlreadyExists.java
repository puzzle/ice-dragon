package ch.puzzle.ln.icedragon.platform.boundary;

import ch.puzzle.ln.web.rest.errors.BadRequestAlertException;
import ch.puzzle.ln.web.rest.errors.ErrorConstants;

public class NameAlreadyExists extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NameAlreadyExists() {
        super(ErrorConstants.PLATFORM_NAME_EXISTS_TYPE, "Platform name already used!", "userManagement", "platformnameexists");
    }
}
