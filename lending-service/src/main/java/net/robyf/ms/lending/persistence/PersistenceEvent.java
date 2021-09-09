package net.robyf.ms.lending.persistence;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import net.robyf.ms.lending.api.Event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "events", indexes = {
        @Index(name = "idx_account_and_time", columnList = "account_id, time", unique = false)
})
public class PersistenceEvent {

    @Id
    @GeneratedValue
    private UUID id;

    @Column (name = "account_id", nullable = false)
    private UUID accountId;

    @Column (name = "time", nullable = false)
    private ZonedDateTime time;

    @Column (name = "type", length = 32, nullable = false)
    private EventType type;

    @Column (name = "amount", precision = 10, scale = 2, nullable = true)
    private BigDecimal amount;

    @Column (name = "resulting_balance", precision = 10, scale = 2, nullable = true)
    private BigDecimal resultingBalance;

    @Tolerate // NOSONAR
    public PersistenceEvent() { // NOSONAR
    }

    public Event asEvent() {
        return Event.builder()
                .id(this.getId())
                .time(this.getTime())
                .type(this.getType().getCode())
                .amount(this.getAmount() != null ? this.getAmount().multiply(BigDecimal.valueOf(this.getType().getMonetaryImpact())) : null)
                .resultingBalance(this.getResultingBalance())
                .build();
    }

}
