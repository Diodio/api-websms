package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MessagesStatsDto {

    private String numberSent;
    private String numberProcess;
    private String numberPending;
    private String numberExpired;
    private String numberRejeted;
    private String numberNotDelivred;
    private String numberNoCredits;
    private String numberDelivred;

    public MessagesStatsDto() {
    }

    public MessagesStatsDto(String numberSent, String numberProcess, String numberPending, String numberExpired, String numberRejeted, String numberNotDelivred, String numberNoCredits, String numberDelivred) {
        this.numberSent = numberSent;
        this.numberProcess = numberProcess;
        this.numberPending = numberPending;
        this.numberExpired = numberExpired;
        this.numberRejeted = numberRejeted;
        this.numberNotDelivred = numberNotDelivred;
        this.numberNoCredits = numberNoCredits;
        this.numberDelivred = numberDelivred;
    }
}
