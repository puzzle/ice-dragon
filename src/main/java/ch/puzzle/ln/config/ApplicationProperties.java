package ch.puzzle.ln.config;

import org.lightningj.lnd.wrapper.MacaroonContext;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Icedragon.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Lnd lnd = new Lnd();

    public Lnd getLnd() {
        return lnd;
    }

    public static class Lnd {

        private String host;
        private int port;
        private String certPath;
        private String adminMacaroonHex;
        private String invoiceMacaroonHex;
        private String readonlyMacaroonHex;
        private Long invoiceExpirySeconds;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getCertPath() {
            return certPath;
        }

        public void setCertPath(String certPath) {
            this.certPath = certPath;
        }

        public String getAdminMacaroonHex() {
            return adminMacaroonHex;
        }

        public MacaroonContext getAdminMacaroonContext() {
            return () -> adminMacaroonHex;
        }
        public void setAdminMacaroonHex(String adminMacaroonHex) {
            this.adminMacaroonHex = adminMacaroonHex;
        }

        public String getInvoiceMacaroonHex() {
            return invoiceMacaroonHex;
        }

        public MacaroonContext getInvoiceMacaroonContext() {
            return () -> invoiceMacaroonHex;
        }

        public void setInvoiceMacaroonHex(String invoiceMacaroonHex) {
            this.invoiceMacaroonHex = invoiceMacaroonHex;
        }

        public String getReadonlyMacaroonHex() {
            return readonlyMacaroonHex;
        }

        public MacaroonContext getReadonlyMacaroonContext() {
            return () -> readonlyMacaroonHex;
        }

        public void setReadonlyMacaroonHex(String readonlyMacaroonHex) {
            this.readonlyMacaroonHex = readonlyMacaroonHex;
        }

        public Long getInvoiceExpirySeconds() {
            return invoiceExpirySeconds;
        }

        public void setInvoiceExpirySeconds(Long invoiceExpirySeconds) {
            this.invoiceExpirySeconds = invoiceExpirySeconds;
        }
    }
}
