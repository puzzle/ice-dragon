package ch.puzzle.ln.icedragon.user.entity;

public class RecklessLogin {
    private String nodePublicKey;
    private String challengeResponse;

    public String getNodePublicKey() {
        return nodePublicKey;
    }

    public void setNodePublicKey(String nodePublicKey) {
        this.nodePublicKey = nodePublicKey;
    }

    public String getChallengeResponse() {
        return challengeResponse;
    }

    public void setChallengeResponse(String challengeResponse) {
        this.challengeResponse = challengeResponse;
    }
}
