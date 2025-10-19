package com.javakull.newmain.GameInfo;

import com.javakull.newmain.Characters.PlayerGameCharacter;

public class ScoreCalculation {

    private PlayerGameCharacter character;
    private boolean taggedNow;
    private boolean taggedBefore;

    public ScoreCalculation(PlayerGameCharacter character) {
        this.character = character;
        taggedNow = character.getTagged();
        taggedBefore = taggedNow;
    }

    public void scoreCheck() {
        taggedNow = character.getTagged();
        if (taggedNow && !taggedBefore) {
            character.setScore(-40);
        } else if (!taggedNow && taggedBefore) {
            character.setScore(30);
        }
        taggedBefore = taggedNow;
    }
}
