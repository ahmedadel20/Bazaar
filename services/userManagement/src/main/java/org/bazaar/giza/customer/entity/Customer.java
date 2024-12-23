package org.bazaar.giza.customer.entity;

import java.util.Set;

import org.bazaar.giza.user.entity.BazaarUser;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
public class Customer extends BazaarUser {
    @ElementCollection
    private Set<Address> addresses;
}
