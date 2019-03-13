package test;

import static org.junit.Assert.*;

import org.junit.Test;

import feats.CharacterFeat;
import feats.Skill;

public class FeatTest {

	@Test
	public void test() {
		CharacterFeat cf = new CharacterFeat();
		assertTrue(cf.getFeatScalar(Skill.HACK) == 5);
		cf.upFeat(Skill.HACK, 10);
		assertTrue(cf.getFeatScalar(Skill.HACK) == 15);
		
		assertTrue(cf.getFeatScalar(Skill.DAMAGE) == 5);
		cf.upDamage(5);
		assertTrue(cf.getFeatScalar(Skill.DAMAGE) == 10);
		cf.upFeat(Skill.DAMAGE, 5);
		assertTrue(cf.getFeatScalar(Skill.DAMAGE) == 15);
		assertTrue(cf.getDamageScalar() == 15);
		cf.upDamage(-5);
		assertTrue(cf.getFeatScalar(Skill.DAMAGE) == 10);
		assertTrue(cf.getDamageScalar() == 10);

	}

}
