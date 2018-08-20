package ru.sbtqa.tag.pagefactory.generators.gens;


public class GeneratorInitializationException extends Exception {

    /**
     * <p>Constructor for PageInitializationException.</p>
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public GeneratorInitializationException(String message, Exception e) {
        super(message, e);
    }

}
