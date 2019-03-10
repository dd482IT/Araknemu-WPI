package fr.quatrevieux.araknemu.network.game.out.account;

import fr.quatrevieux.araknemu.data.constant.Characteristic;
import fr.quatrevieux.araknemu.game.player.CharacterProperties;

/**
 * Send to client current player stats
 *
 * https://github.com/Emudofus/Dofus/blob/1.29/dofus/aks/Account.as#L797
 */
final public class Stats {
    final private CharacterProperties player;

    public Stats(CharacterProperties player) {
        this.player = player;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("As");

        sb
            .append(player.experience().current()).append(',').append(player.experience().min()).append(',').append(player.experience().max()).append('|')
            .append(player.kamas()).append("|") // Kamas
            .append(player.characteristics().boostPoints()).append("|")
            .append(player.spells().upgradePoints()).append("|")
            .append('|') // Align
            .append(player.life().current()).append(',').append(player.life().max()).append('|')
            .append("10000,10000|") // Energy, Energy Max
            .append(player.characteristics().initiative()).append('|')
            .append(player.characteristics().discernment()).append('|')
        ;

        for (Characteristic characteristic : Characteristic.values()) {
            sb
                .append(player.characteristics().base().get(characteristic)).append(',')
                .append(player.characteristics().stuff().get(characteristic)).append(',')
                .append(player.characteristics().feats().get(characteristic)).append(',')
                .append(player.characteristics().boost().get(characteristic)).append('|')
            ;
        }

        return sb.toString();
    }
}
