package ch.puzzle.ln.icedragon.lnd.boundary;

public interface InvoiceHandler {

    void handleInvoiceUpdated(String hashHex, org.lightningj.lnd.wrapper.message.Invoice invoice);


}
