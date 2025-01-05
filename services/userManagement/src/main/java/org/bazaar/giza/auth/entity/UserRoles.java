package org.bazaar.giza.auth.entity;

import java.util.Set;

import org.bazaar.giza.user.entity.BazaarUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
// Table Generation can be improved
public class UserRoles {
    @Id
    private Long userId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @MapsId
    @JoinColumn(name = "user_id")
    private BazaarUser user;
    @ElementCollection
    @CollectionTable(name = "roles")
    private Set<String> roles;

    public UserRoles(BazaarUser user, Set<String> roles) {
        this.user = user;
        this.roles = roles;
    }
}
