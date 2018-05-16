package fr.quatrevieux.araknemu.game.spell;

import fr.quatrevieux.araknemu.data.world.entity.SpellTemplate;
import fr.quatrevieux.araknemu.data.world.repository.SpellTemplateRepository;
import fr.quatrevieux.araknemu.game.PreloadableService;
import fr.quatrevieux.araknemu.game.spell.adapter.SpellLevelAdapter;
import fr.quatrevieux.araknemu.game.spell.effect.SpellEffectService;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Service for handle spells
 */
final public class SpellService implements PreloadableService {
    final private SpellTemplateRepository repository;
    final private SpellEffectService effectService;

    final private ConcurrentMap<Integer, SpellLevels> spells = new ConcurrentHashMap<>();

    public SpellService(SpellTemplateRepository repository, SpellEffectService effectService) {
        this.repository = repository;
        this.effectService = effectService;
    }

    @Override
    public void preload(Logger logger) {
        logger.info("Loading spells...");

        for (SpellTemplate template : repository.load()) {
            spells.put(template.id(), load(template));
        }

        logger.info("{} spells loaded", spells.size());
    }

    /**
     * Get a spell
     *
     * @param spellId The spell id
     */
    public SpellLevels get(int spellId) {
        if (!spells.containsKey(spellId)) {
            spells.put(spellId, load(repository.get(spellId)));
        }

        return spells.get(spellId);
    }

    /**
     * Load the spell levels
     *
     * @param template The spell template
     */
    private SpellLevels load(SpellTemplate template) {
        List<Spell> levels = new ArrayList<>(template.levels().length);

        for (int i = 0; i < template.levels().length; ++i) {
            SpellTemplate.Level level = template.levels()[i];

            if (level == null) {
                break;
            }

            levels.add(makeLevel(i + 1, template, level));
        }

        return new SpellLevels(template, levels.toArray(new Spell[levels.size()]));
    }

    /**
     * Make one spell level
     */
    private Spell makeLevel(int level, SpellTemplate template, SpellTemplate.Level data) {
        return new SpellLevelAdapter(
            level,
            template,
            data,
            effectService.makeAll(data.effects(), data.effectAreas(), template.targets()),
            effectService.makeAll(
                data.criticalEffects(),
                data.effectAreas().subList(data.effects().size(), data.effectAreas().size()),
                template.targets()
            )
        );
    }
}
