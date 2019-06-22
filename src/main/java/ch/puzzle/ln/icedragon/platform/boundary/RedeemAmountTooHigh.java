package ch.puzzle.ln.icedragon.platform.boundary;

import ch.puzzle.ln.web.rest.errors.BadRequestAlertException;
import ch.puzzle.ln.web.rest.errors.ErrorConstants;

public class RedeemAmountTooHigh extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public RedeemAmountTooHigh() {
        super(ErrorConstants.PLATFORM_URL_EXISTS_TYPE, "Redeem invoice amount is too high!", "dashboard", "invoiceamounttoohigh");
    }
}
