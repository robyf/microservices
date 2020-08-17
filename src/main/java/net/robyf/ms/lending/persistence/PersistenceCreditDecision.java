package net.robyf.ms.lending.persistence;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import net.robyf.ms.lending.api.CreditDecision;
import net.robyf.ms.lending.api.CreditDecisionStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "credit_decisions", indexes = {
        @Index(name = "idx_account_status", columnList = "account_id,status", unique = false)
})
public class PersistenceCreditDecision {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "status", length = 32, nullable = false)
    @Enumerated(EnumType.STRING)
    private CreditDecisionStatus status;

    @Column(name = "positive", nullable = false)
    private boolean positive;

    @Column(name = "scoring_id", nullable = false)
    private UUID scoringId;

    @Column (name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Tolerate
    public PersistenceCreditDecision() {
    }

    public CreditDecision asCreditDecision() {
        return CreditDecision.builder()
                .id(this.getId())
                .accountId(this.getAccountId())
                .status(this.getStatus())
                .positive(this.isPositive())
                .scoringId(this.getScoringId())
                .amount(this.getAmount())
                .build();
    }

}
