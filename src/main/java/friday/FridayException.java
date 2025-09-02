package friday;

/**
 * Custom exception for Friday application errors.
 */
class FridayException extends Exception {

    /**
     * Constructs a FridayException with the given message.
     *
     * @param message The error message.
     */
    FridayException(String message) {
        super(message);
    }
}
