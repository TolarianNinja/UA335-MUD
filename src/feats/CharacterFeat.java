package feats;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class CharacterFeat extends Feat {

	private static final long serialVersionUID = -4228379856269926144L;

	private HashMap<Skill, Integer> feats;
	public CharacterFeat() {
		super(5);
		feats = new HashMap<Skill, Integer>();
		feats.put(Skill.HACK, 5);
		feats.put(Skill.MEDICAL, 5);
		feats.put(Skill.DAMAGE, getDamageScalar());
	}

	/**
	 * @param skillToAdd
	 */
	public void addFeat(Skill skillToAdd) {
		if (findSkillEntry(skillToAdd) == null)
			feats.put(skillToAdd, 5);
	}

	/**
	 * @return the set of all feats with their corresponding values
	 */
	public Set<Entry<Skill, Integer>> getMapSet() {
		return feats.entrySet();
	}

	/**
	 * @param skill
	 * @return the entry for the skill found (if one such exists)
	 */
	public Entry<Skill, Integer> findSkillEntry(Skill skill) {
		Set<Entry<Skill, Integer>> set = getMapSet();
		for (Entry<Skill, Integer> entry : set) {
			if (entry.getKey().equals(skill))
				return entry;
		}
		// wasn't a talent the character had
		return null;
	}

	/**
	 * @param skill
	 * @return get scalar for a particular skill/feat
	 */
	public int getFeatScalar(Skill skill) {
		if (skill.equals(Skill.DAMAGE))
			return getDamageScalar();

		Entry<Skill, Integer> e = findSkillEntry(skill);
		return (int) e.getValue();
	}

	/**
	 * @param skillToUp
	 * @param n
	 *            add skillToUp by n
	 */
	public void upFeat(Skill skillToUp, int n) {
		int i = (int) findSkillEntry(skillToUp).getValue();
		if ((i + n) > 100 || (i + n) < 0)
			return;

		if (skillToUp.equals(Skill.DAMAGE)) {
			upDamage(n);
			return;
		}

		feats.put(skillToUp, (i + n));
	}

	/**
	 * @param itemDifficulty
	 * @return true if character's hacking ability is sufficient for hacking an
	 *         item of itemDifficulty, with a tolerance of plus 5 and minus 5.
	 */
	public boolean isHackSuccess(int itemDifficulty) {
		int hackScal = findSkillEntry(Skill.HACK).getValue();
		return ((itemDifficulty - 5) <= hackScal)
				&& (hackScal <= (itemDifficulty + 5));
	}

	@Override
	public int calculateLevel() {
		int totalSkillpts = getTotal();
		return (totalSkillpts / getNumEntries()) / 10;
	}

	private int getNumEntries() {
		return getMapSet().size();
	}
	
	private int getTotal() {
		int i = 0;
		Set<Entry<Skill, Integer>> s = getMapSet();
		for (Entry<Skill, Integer> entry : s) {
			i += entry.getValue();
		}
		return i;
	}

}
