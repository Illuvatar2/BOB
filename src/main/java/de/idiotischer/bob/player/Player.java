package de.idiotischer.bob.player;

import de.idiotischer.bob.country.Country;
import de.idiotischer.bob.troop.Troop;

import java.net.InetSocketAddress;
import java.util.List;

public interface Player {
    Country country();
    void country(Country country);

    List<Troop> selectedTroops();

    InetSocketAddress address();
}
