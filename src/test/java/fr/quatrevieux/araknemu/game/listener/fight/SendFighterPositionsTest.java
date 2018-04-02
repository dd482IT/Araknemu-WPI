package fr.quatrevieux.araknemu.game.listener.fight;

import fr.quatrevieux.araknemu.game.GameBaseCase;
import fr.quatrevieux.araknemu.game.exploration.map.ExplorationMapService;
import fr.quatrevieux.araknemu.game.fight.Fight;
import fr.quatrevieux.araknemu.game.fight.FightService;
import fr.quatrevieux.araknemu.game.fight.builder.ChallengeBuilder;
import fr.quatrevieux.araknemu.game.fight.event.FighterPlaceChanged;
import fr.quatrevieux.araknemu.network.game.out.fight.FighterPositions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SendFighterPositionsTest extends GameBaseCase {
    private Fight fight;
    private SendFighterPositions listener;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        dataSet.pushMaps();

        fight = container.get(FightService.class).handler(ChallengeBuilder.class)
            .start(builder -> {
                try {
                    builder
                        .fighter(gamePlayer(true))
                        .fighter(makeOtherPlayer())
                        .map(container.get(ExplorationMapService.class).load(10340))
                    ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            })
        ;

        listener = new SendFighterPositions(fight);
        requestStack.clear();
    }

    @Test
    void onFighterPlaceChanged() {
        listener.on(new FighterPlaceChanged(fight.fighters().get(0)));

        requestStack.assertLast(new FighterPositions(fight.fighters()));
    }
}