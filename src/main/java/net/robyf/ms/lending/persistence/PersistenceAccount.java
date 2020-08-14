package net.robyf.ms.lending.persistence;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.AccountStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "accounts", indexes = {
        @Index(name = "idx_user", columnList = "user_id", unique = true)
})
public class PersistenceAccount {

    @Id
    @GeneratedValue
    private UUID id;

    @Column (name = "user_id", nullable = false)
    private UUID userId;

    @Column (name = "status", length = 32, nullable = false)
    private AccountStatus status;

    @Column (name = "balance", length = 10, precision = 2, nullable = false)
    private BigDecimal balance;

    @Tolerate
    public PersistenceAccount() {
    }

    public Account asAccount() {
        return Account.builder()
                .id(this.getId())
                .userId(this.getUserId())
                .status(this.getStatus())
                .balance(this.getBalance())
                .build();
    }

}
