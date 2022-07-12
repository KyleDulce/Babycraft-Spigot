package me.someoneawesome.babycraft.model.child;

import java.util.Random;

public enum ActionMessage {
    HUG(new String[] {
            "*hugs back* I love you too {parent-gender}!",
            "Thank you {parent-gender}, Love you too!",
            "{parent-gender} thank you!"
    }, new String[] {
            "No no no, let me go {parent-gender}!",
            "Stop embarrassing me {parent-gender}!",
            "Why again {parent-gender}"
    }),

    KISS(new String[] {
            "*kisses back* I love you too {parent-gender}!",
            "*smiles* Love you {parent-gender}!",
            "{parent-gender}, love youuu!"
    }, new String[] {
            "{parent-gender} noo you are embarrassing me!",
            "{parent-gender} no please {parent-gender} noo do not *pushes away* no!",
            "You always do that in front of my friends {parent-gender} stop it"
    }),

    JOKE(new String[] {
            "hahaha, You are very funny {parent-gender}!",
            "haha i get it!",
            "Thats so funny {parent-gender}!"
    }, new String[] {
            "You tell me that one all the time {parent-gender}, its not funny anymore",
            "ughh, that one again {parent-gender}?",
            "You are not funny {parent-gender}"
    }),

    CHAT(new String[] {
            "I love your stories {parent-gender}",
            "Love you!",
            "Thank you {parent-gender}",
            "Can we go now {parent-gender}, Pleaseeeede",
            "You are the best {parent-gender}",
            "Can i go play with the other kids {parent-gender}?",
            "Can we play?",
            "Hi {parent-gender}",
            "{parent-gender}, can we have a pet?",
            "{parent-gender} i want a doggy!",
            "Bye {parent-gender}",
            "I'm Hungry",
            "I'm Thirsty",
            "Ok {parent-gender}"
    }, new String[] {
            "{parent-gender} you told me that a million timesss",
            "I am not a baby {parent-gender}",
            "Whaat, no please {parent-gender}, i do not want to, no pleasee",
            "NOO! you always tell me to do that {parent-gender}, i do not want to please",
            "I'm bored",
            "I hate You",
            "{parent-gender}, you are mean, i want to play!",
            "*cries* no no no, i want this!"
    }),

    CHANGE_CLOTHES(new String[] {
            "Yay",
            "I look different now {parent-gender}",
            "This looks good",
            "I love these clothes! I will brag about it to my friends!",
            "Thanks {parent-gender}"
    }, new String[] {
            "really, these clothes are sooo last year",
            "{parent-gender}, again, i don't like to wear this!",
            "{parent-gender}, you have no sense of fashion",
            "i don't like it",
            "*rolls eyes* really?"
    }),

    ATTACKED(new String[] {
            "Oww, why did you do that {parent-gender}!"
    }),

    FOLLOW(new String[] {
            "Ok i will follow you {parent-gender}"
    }),

    STAY(new String[] {
            "Ok {parent-gender} I will stay here"
    }),

    ROAM(new String[] {
            "yay lets go play {parent-gender}!"
    }),

    EASTER_EGG(new String[] {
            "{parent-gender}, I feel weird around this {opposite-own-gender}, its like {opposite-sub-pronoun} makes me happy but i don't know why... WHAT! no i do not love them, {parent-gender} thats gross!",
            "{parent-gender}, I think i like this one {opposite-own-gender}",
            "{parent-gender}, I want to get married to my {lover} right now!",
            "{parent-gender}, are you cheating on {opposite-parent-gender}",
            "{parent-gender}, I have a {lover} you know?",
            "WHAT! no i will never have a {lover}, and i will never get married",
            "{parent-gender}, why do dogs sniff other dog's butts?",
            "{parent-gender}, how are babies made?"
    }),

    NULL(new String[] {
            "[error 404: message not found]"
    });
    private final String[] positiveMessages;
    private final String[] negativeMessages;
    private ActionMessage(String[] positiveMessages, String[] negativeMessages) {
        this.positiveMessages = positiveMessages;
        this.negativeMessages = negativeMessages;
    }

    private ActionMessage(String[] messages) {
        this.positiveMessages = messages;
        this.negativeMessages = new String[0];
    }

    public String nextMessage() {
        int number = new Random().nextInt(positiveMessages.length + negativeMessages.length);
        if(number >= positiveMessages.length) {
            return negativeMessages[number - positiveMessages.length];
        } else {
            return positiveMessages[number];
        }
    }
}
