package fr.quatrevieux.araknemu.game.handler;

import fr.quatrevieux.araknemu.common.account.Permission;
import fr.quatrevieux.araknemu.core.di.ContainerException;
import fr.quatrevieux.araknemu.data.constant.Race;
import fr.quatrevieux.araknemu.data.living.entity.account.Account;
import fr.quatrevieux.araknemu.data.world.repository.character.PlayerRaceRepository;
import fr.quatrevieux.araknemu.game.GameBaseCase;
import fr.quatrevieux.araknemu.game.account.AccountService;
import fr.quatrevieux.araknemu.game.account.CharactersService;
import fr.quatrevieux.araknemu.game.account.GameAccount;
import fr.quatrevieux.araknemu.game.handler.account.ListCharacters;
import fr.quatrevieux.araknemu.game.player.GamePlayer;
import fr.quatrevieux.araknemu.game.player.PlayerService;
import fr.quatrevieux.araknemu.network.exception.CloseImmediately;
import fr.quatrevieux.araknemu.network.game.in.account.AskCharacterList;
import fr.quatrevieux.araknemu.network.in.Packet;
import fr.quatrevieux.araknemu.network.in.PacketHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnsureAdminTest extends GameBaseCase {
    @Test
    void handleNotLogged() {
        PacketHandler inner = Mockito.mock(PacketHandler.class);
        EnsureAdmin handler = new EnsureAdmin<>(inner);

        assertThrows(CloseImmediately.class, () -> handler.handle(session, Mockito.mock(Packet.class)));
    }

    @Test
    void handleSuccess() throws Exception {
        PacketHandler inner = Mockito.mock(PacketHandler.class);
        EnsureAdmin handler = new EnsureAdmin<>(inner);
        session.attach(
            new GameAccount(
                new Account(1, "", "", "", EnumSet.allOf(Permission.class), "", ""),
                container.get(AccountService.class),
                1
            )
        );

        dataSet.pushRaces();

        session.setPlayer(
            new GamePlayer(
                session.account(),
                dataSet.createPlayer(5),
                container.get(PlayerRaceRepository.class).get(Race.FECA),
                session,
                container.get(PlayerService.class)
            )
        );

        Packet packet = new AskCharacterList(false);

        handler.handle(session, packet);

        Mockito.verify(inner).handle(session, packet);
    }

    @Test
    void handleNotAdmin() throws Exception {
        PacketHandler inner = Mockito.mock(PacketHandler.class);
        EnsureAdmin handler = new EnsureAdmin<>(inner);
        session.attach(
            new GameAccount(
                new Account(1, "", "", "", EnumSet.noneOf(Permission.class), "", ""),
                container.get(AccountService.class),
                1
            )
        );

        assertThrows(CloseImmediately.class, () -> handler.handle(session, Mockito.mock(Packet.class)));
    }

    @Test
    void packet() throws ContainerException {
        assertEquals(
            AskCharacterList.class,
            new EnsureAdmin<>(new ListCharacters(
                container.get(CharactersService.class)
            )).packet()
        );
    }
}