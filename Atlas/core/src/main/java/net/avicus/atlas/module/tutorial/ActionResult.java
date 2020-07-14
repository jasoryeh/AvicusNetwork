package net.avicus.atlas.module.tutorial;

public enum ActionResult {
    /**
     * All chat and commands are allowed and can be done freely. Players
     * will remain in the tutorial regardless of what happens.
     */
    ALLOW,

    /**
     * All chat and commands performed by the user will be cancelled.
     */
    DISALLOW,

    /**
     * Upon chatting or using a command, the user will immediately
     * exit the tutorial.
     */
    EXIT
}
