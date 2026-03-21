package de.idiotischer.bob.player;

import de.idiotischer.bob.country.Country;

import java.net.InetSocketAddress;

public class ServerPlayer extends LocalPlayer {
    private final InetSocketAddress address;

    public ServerPlayer(InetSocketAddress address, Country country) {
        super(country);
        this.address = address;
    }

    @Override
    public InetSocketAddress address() {
        return address;
    }
}
